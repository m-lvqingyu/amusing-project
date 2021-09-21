package com.amusing.start.client.api;

import com.amusing.start.client.fallback.UserClientFallback;
import com.amusing.start.client.input.UserSettlementInput;
import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@FeignClient(name = "amusing-user-server", fallbackFactory = UserClientFallback.class)
@RequestMapping("user")
public interface UserClient {

    /**
     * 根据用户唯一ID，获取账户信息
     *
     * @param userId
     * @return
     */
    @GetMapping("account/{id}")
    UserAccountOutput account(@PathVariable("id") String userId);

    /**
     * 账户结算
     *
     * @param input
     * @return
     */
    @PostMapping("settlement")
    ApiResult userSettlement(@RequestBody UserSettlementInput input);

}
