package com.amusing.start.client.fallback;

import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.input.PayInput;
import com.amusing.start.client.output.AccountOutput;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

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
            public AccountOutput account(String userId) throws CustomException {
                throw new CustomException(ErrorCode.DEGRADE_ERR);
            }

            @Override
            public Boolean payment(PayInput input) throws CustomException {
                throw new CustomException(ErrorCode.DEGRADE_ERR);
            }

            @Override
            public Boolean matchPath(String userId, String path) throws CustomException {
                throw new CustomException(ErrorCode.DEGRADE_ERR);
            }
        };
    }
}
