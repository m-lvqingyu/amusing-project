package com.amusing.start.client.fallback;

import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.input.PayInput;
import com.amusing.start.client.output.AccountOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.exception.CustomException;
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
            public ApiResult<AccountOutput> accountInfo(String userId) throws CustomException {
                throw new CustomException(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<Integer> accountBalance(String userId) throws CustomException {
                throw new CustomException(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<Boolean> payment(PayInput input) throws CustomException {
                throw new CustomException(CommCode.DEGRADE_ERR);
            }
        };
    }
}
