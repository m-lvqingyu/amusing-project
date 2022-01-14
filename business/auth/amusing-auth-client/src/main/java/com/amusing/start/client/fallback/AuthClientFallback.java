package com.amusing.start.client.fallback;

import com.amusing.start.client.api.AuthClient;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lv.qingyu
 */
@Slf4j
@Component
public class AuthClientFallback implements FallbackFactory<AuthClient> {
    @Override
    public AuthClient create(Throwable throwable) {
        return new AuthClient() {
            @Override
            public ApiResult<Boolean> isValid(String userId) {
                log.error("[auth]-isValid fallback! userId:{}", userId);
                return ApiResult.result(CommCode.DEGRADE_ERROR);
            }
        };
    }
}
