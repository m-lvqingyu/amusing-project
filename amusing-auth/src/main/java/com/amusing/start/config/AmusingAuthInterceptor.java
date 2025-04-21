package com.amusing.start.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.amusing.start.client.api.UserFeignClient;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.exception.InnerApiException;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2021/09/04
 */
@Slf4j
@Component
public class AmusingAuthInterceptor implements HandlerInterceptor {

    private final AntPathMatcher antPathMatcher;

    private final IgnoreAuthPathProps ignoreAuthPathProps;

    private final UserFeignClient userFeignClient;

    public AmusingAuthInterceptor(AntPathMatcher antPathMatcher,
                                  IgnoreAuthPathProps ignoreAuthPathProps,
                                  UserFeignClient userFeignClient) {
        this.antPathMatcher = antPathMatcher;
        this.ignoreAuthPathProps = ignoreAuthPathProps;
        this.userFeignClient = userFeignClient;
    }

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request,
                             @Nonnull HttpServletResponse response,
                             @Nonnull Object handler) {
        String uri = request.getRequestURI();
        List<String> paths = ignoreAuthPathProps.getPaths();
        if (CollectionUtil.isNotEmpty(paths)) {
            for (String path : paths) {
                if (antPathMatcher.match(path, uri)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.TRUE;
//        String userId = request.getHeader(CommConstant.USER_UID);
//        if (StringUtils.isBlank(userId)) {
//            throw new CustomException(CommunalCode.UNAUTHORIZED);
//        }
//        String version = request.getHeader(CommConstant.LOGIN_VERSION);
//        if (StringUtils.isBlank(version) || !NumberUtil.isNumber(version)) {
//            throw new CustomException(CommunalCode.UNAUTHORIZED);
//        }
//        ApiResult<Boolean> apiResult = userFeignClient.auth(userId, Long.parseLong(version), uri);
//        if (apiResult.isSuccess()) {
//            throw new InnerApiException(apiResult.getCode(), apiResult.getMessage());
//        }
//        if (apiResult.getData()) {
//            return Boolean.TRUE;
//        }
//        throw new CustomException(CommunalCode.UNAUTHORIZED);
    }

}
