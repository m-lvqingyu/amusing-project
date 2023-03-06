package com.amusing.start.client.api;

import com.amusing.start.client.fallback.UserClientFallback;
import com.amusing.start.client.input.PayInput;
import com.amusing.start.client.output.AccountOutput;
import com.amusing.start.exception.CustomException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

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
    AccountOutput account(@PathVariable("id") String userId) throws CustomException;

    /**
     * 主账户结算
     *
     * @param input 支付信息
     * @return true:成功 false:失败
     */
    @PostMapping("/user/inward/v1/pay")
    Boolean payment(@RequestBody PayInput input) throws CustomException;

    /**
     * @param userId 用户ID
     * @return 角色码集合
     * @throws CustomException
     */
    @GetMapping("/user/inward/v1/match/path")
    Boolean matchPath(@RequestParam("userId") String userId, @RequestParam("path") String path) throws CustomException;

}
