package com.amusing.start.client.fallback;

import com.amusing.start.client.api.UserFeignClient;
import com.amusing.start.client.request.AccountPayRequest;
import com.amusing.start.client.request.ConsigneeInfoRequest;
import com.amusing.start.client.response.ConsigneeResp;
import com.amusing.start.client.response.UserDetailResp;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@Slf4j
@Component
public class UserFeignClientFallback implements FallbackFactory<UserFeignClient> {

    @Override
    public UserFeignClient create(Throwable throwable) {

        return new UserFeignClient() {

            @Override
            public ApiResult<UserDetailResp> detail(String userId) {
                return ApiResult.result(CommunalCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<ConsigneeResp> consigneeInfo(ConsigneeInfoRequest request) {
                return ApiResult.result(CommunalCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<Integer> balance(String userId) {
                return ApiResult.result(CommunalCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<Boolean> payment(AccountPayRequest request) {
                return ApiResult.result(CommunalCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<Boolean> auth(String userId, Long version, String uri) {
                return ApiResult.result(CommunalCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<List<String>> getDataScope(String userId) {
                return ApiResult.result(CommunalCode.DEGRADE_ERR);
            }
        };
    }

}
