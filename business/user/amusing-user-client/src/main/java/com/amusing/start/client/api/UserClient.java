package com.amusing.start.client.api;

import com.amusing.start.client.fallback.UserClientFallback;
import com.amusing.start.client.input.PayInput;
import com.amusing.start.client.output.AccountOutput;
import com.amusing.start.exception.CustomException;
import com.amusing.start.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@FeignClient(name = "amusing-user-server", fallbackFactory = UserClientFallback.class)
public interface UserClient {

    /**
     * 根据用户唯一ID，获取账户信息
     *
     * @param userId 用户ID
     * @return 账户信息
     */
    @GetMapping("/user/inward/v1/account/{id}")
    ApiResult<AccountOutput> account(@PathVariable("id") String userId) throws CustomException;

    /**
     * 主账户结算
     *
     * @param input 支付信息
     * @return true:成功 false:失败
     */
    @PostMapping("/user/inward/v1/pay")
    ApiResult<Boolean> pay(@RequestBody PayInput input) throws CustomException;

}
