package com.amusing.start.client.fallback;

import com.amusing.start.client.api.AccountFeignClient;
import com.amusing.start.client.request.AccountPayRequest;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@Slf4j
@Component
public class AccountFeignClientFallback implements FallbackFactory<AccountFeignClient> {
    @Override
    public AccountFeignClient create(Throwable throwable) {
        return new AccountFeignClient() {

            @Override
            public ApiResult<Integer> amount(String userId) {
                return ApiResult.result(CommunalCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<?> payment(AccountPayRequest request) {
                return ApiResult.result(CommunalCode.DEGRADE_ERR);
            }
        };
    }
}
