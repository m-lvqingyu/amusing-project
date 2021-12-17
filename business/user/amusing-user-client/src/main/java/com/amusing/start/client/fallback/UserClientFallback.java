package com.amusing.start.client.fallback;

import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.input.UserSettlementInput;
import com.amusing.start.client.output.UserAccountOutput;
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
                log.error("[user]-getAccount fallback! userId:{}", userId);
                return null;
            }

            @Override
            public boolean userMainSettlement(UserSettlementInput input) {
                log.error("[user]-userMainSettlement fallback! param:{}", input);
                return false;
            }

            @Override
            public boolean userGiveSettlement(UserSettlementInput input) {
                log.error("[user]-userGiveSettlement fallback! param:{}", input);
                return false;
            }

        };
    }
}
