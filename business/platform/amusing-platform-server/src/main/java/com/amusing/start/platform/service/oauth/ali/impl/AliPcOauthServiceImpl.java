package com.amusing.start.platform.service.oauth.ali.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.amusing.start.code.CommCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.platform.service.oauth.ali.AliPcOauthService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/11/10
 */
@Slf4j
@Service
public class AliPcOauthServiceImpl implements AliPcOauthService {

    @Value("${ali.pay.app.id}")
    private String aliPayAppId;

    private final AlipayClient alipayClient;

    @Autowired
    public AliPcOauthServiceImpl(AlipayClient alipayClient) {
        this.alipayClient = alipayClient;
    }

    /**
     * 值为authorization_code时，代表用 code 换取
     */
    private static final String AUTHORIZATION_CODE = "authorization_code";

    /**
     * 值为refresh_token时，代表用 refresh_token 换取
     */
    private static final String REFRESH_TOKEN = "refresh_token";

    @Override
    public String getUserId(String appId, String authCode) {
        if (!aliPayAppId.equals(appId)) {
            throw new CustomException(CommCode.PARAMETER_ERR);
        }
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(authCode);
        request.setGrantType(AUTHORIZATION_CODE);
        request.setRefreshToken(authCode);
        AlipaySystemOauthTokenResponse response;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error("[AliPcOauth]-getUserId err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        log.info("[AliPcOauth]-getUserId result:{}", JSONObject.toJSONString(response));
        if (!response.isSuccess()) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        return response.getUserId();
    }

}
