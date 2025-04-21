package com.amusing.start.client.api;

import com.amusing.start.client.fallback.AccountFeignClientFallback;
import com.amusing.start.client.request.AccountPayRequest;
import com.amusing.start.exception.CustomException;
import com.amusing.start.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Lv.QingYu
 * @since 2024/3/12
 */
@FeignClient(name = "amusing-user-server",
        fallbackFactory = AccountFeignClientFallback.class,
        contextId = "account-client")
public interface AccountFeignClient {

    /**
     * 获取账户余额
     *
     * @param userId 用户ID
     * @return 账户余额
     */
    @GetMapping("/inward/account/amount/{id}")
    ApiResult<Integer> amount(@PathVariable("id") String userId);

    /**
     * 主账户结算
     *
     * @param request 支付信息
     */
    @PostMapping("/inward/account/payment")
    ApiResult<?> payment(@RequestBody AccountPayRequest request);


}
