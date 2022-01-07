package com.amusing.start.client.api;

import com.amusing.start.client.fallback.UserClientFallback;
import com.amusing.start.client.input.UserSettlementInput;
import com.amusing.start.client.output.UserAccountOutput;
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
     * 账户信息初始化
     *
     * @param userId 用户ID
     * @return true:成功  false:失败
     */
    @GetMapping("/user/inward/v1/init/{id}")
    ApiResult<Boolean> init(@PathVariable("id") String userId);

    /**
     * 根据用户唯一ID，获取账户信息
     *
     * @param userId 用户ID
     * @return 账户信息
     */
    @GetMapping("/user/inward/v1/account/{id}")
    UserAccountOutput account(@PathVariable("id") String userId);

    /**
     * 主账户结算
     *
     * @param input 支付信息
     * @return true:成功 false:失败
     */
    @PostMapping("/user/inward/v1/main/settlement")
    boolean userMainSettlement(@RequestBody UserSettlementInput input);

    /**
     * 副账户结算
     *
     * @param input 支付信息
     * @return true:成功 false:失败
     */
    @PostMapping("/user/inward/v1/give/settlement")
    boolean userGiveSettlement(@RequestBody UserSettlementInput input);

}
