package com.amusing.start.platform.service.ding.impl;

import com.amusing.start.client.output.DingTalkAccessToken;
import com.amusing.start.code.CommCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.platform.constant.CacheKey;
import com.amusing.start.platform.constant.DingTalkConstant;
import com.amusing.start.platform.constant.DingTalkErrorCode;
import com.amusing.start.platform.enums.code.PlatformErrorCode;
import com.amusing.start.platform.service.ding.IDingTalkUserService;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiV2UserGetbymobileRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiV2UserGetbymobileResponse;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author Lv.QingYu
 * @since 2023/10/3
 */
@Slf4j
@Service
public class DingTalkUserServiceImpl implements IDingTalkUserService {

    @Value("${ding.talk.app.key}")
    private String dingTalkAppKey;

    @Value("${ding.talk.app.secret}")
    private String dingTalkAppSecret;

    private final RedissonClient redissonClient;

    @Autowired
    public DingTalkUserServiceImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public DingTalkAccessToken getDingTalkAccessToken() {
        String accessToken = getAccessToken();
        return new DingTalkAccessToken().setAccessToken(accessToken);
    }

    @Override
    public String getDingTalkUserId(String phone) {
        String accessToken = getAccessToken();
        DingTalkClient dingTalkClient = getDingTalkClient(DingTalkConstant.GET_USER_ID_PATH);
        OapiV2UserGetbymobileRequest req = new OapiV2UserGetbymobileRequest();
        req.setMobile(phone);
        OapiV2UserGetbymobileResponse response;
        try {
            response = dingTalkClient.execute(req, accessToken);
        } catch (Exception e) {
            log.error("[GetDingTalkUserId]-err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        errorCodeHandling(response.getErrcode());
        return response.getResult().getUserid();
    }

    private String getAccessToken() {
        String key = CacheKey.getDingTalkAccessTokenKey();
        RBucket<String> bucket = redissonClient.getBucket(key);
        String value = bucket.get();
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        DingTalkClient client = getDingTalkClient(DingTalkConstant.GET_TOKEN_PATH);
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(dingTalkAppKey);
        request.setAppsecret(dingTalkAppSecret);
        request.setHttpMethod(HttpMethod.GET.name());
        OapiGettokenResponse response;
        try {
            response = client.execute(request);
        } catch (Exception e) {
            log.error("[DingTalkAccessToken]-err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        value = response.getAccessToken();
        if (StringUtils.isBlank(value)) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        bucket.set(value, CommConstant.THIRTY, TimeUnit.MINUTES);
        return value;
    }

    private void errorCodeHandling(Long errCode) {
        if (errCode == DingTalkErrorCode.SUCCESS) {
            return;
        }
        if (errCode == DingTalkErrorCode.SYSTEM_BUSY) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        if (errCode == DingTalkErrorCode.USER_NOT_FOUND) {
            throw new CustomException(PlatformErrorCode.USER_NOT_FOUND);
        }
        if (errCode == DingTalkErrorCode.INVALID_PHONE) {
            throw new CustomException(PlatformErrorCode.INVALID_PHONE);
        }
        if (errCode == DingTalkErrorCode.INVALID_PARAM) {
            throw new CustomException(PlatformErrorCode.INVALID_PARAM);
        }
        throw new CustomException(CommCode.SERVICE_ERR);
    }

    private DingTalkClient getDingTalkClient(String url) {
        return new DefaultDingTalkClient(url);
    }

}
