package com.amusing.start.client.fallback;

import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.input.UserSettlementInput;
import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.result.ApiCode;
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
            public UserAccountOutput account(String userId) {
                log.error("[user-client]-根据用户ID:{}获取账户信息降级！", userId);
                return null;
            }

            @Override
            public ApiResult userSettlement(UserSettlementInput input) {
                log.error("[user-client]-用户账户：{}结算降级！", input);
                return ApiResult.fail(ApiCode.DEGRADE_ERROR);
            }
        };
    }
}
