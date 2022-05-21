package com.amusing.start.client.fallback;

import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.input.UserSettlementInput;
import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@Slf4j
@Component
public class UserClientFallback implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable throwable) {
        return new UserClient() {

            @Override
            public ApiResult<Boolean> init(String userId) {
                log.error("[user]-initAccount fallback! userId:{}", userId);
                return ApiResult.result(CommCode.DEGRADE_ERROR);
            }

            @Override
            public ApiResult<UserAccountOutput> account(String userId) {
                log.error("[user]-getAccount fallback! userId:{}", userId);
                return ApiResult.result(CommCode.DEGRADE_ERROR);
            }

            @Override
            public ApiResult<Boolean> mainSettlement(UserSettlementInput input) {
                log.error("[user]-mainSettlement fallback! param:{}", input);
                return ApiResult.result(CommCode.DEGRADE_ERROR);
            }
        };
    }
}
