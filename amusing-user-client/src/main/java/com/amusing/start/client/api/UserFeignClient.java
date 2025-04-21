package com.amusing.start.client.api;

import com.amusing.start.client.fallback.UserFeignClientFallback;
import com.amusing.start.client.request.AccountPayRequest;
import com.amusing.start.client.request.ConsigneeInfoRequest;
import com.amusing.start.client.response.ConsigneeResp;
import com.amusing.start.client.response.UserDetailResp;
import com.amusing.start.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@FeignClient(name = "amusing-user-server", fallbackFactory = UserFeignClientFallback.class, contextId = "user-client")
public interface UserFeignClient {

    @GetMapping("/inward/user/info/detail/{id}")
    ApiResult<UserDetailResp> detail(@PathVariable("id") String userId);

    /**
     * 获取收获地址信息
     *
     * @param request 用户ID等
     * @return 收获地址信息
     */
    @PostMapping("/inward/user/consignee/info")
    ApiResult<ConsigneeResp> consigneeInfo(@RequestBody ConsigneeInfoRequest request);

    /**
     * 获取账户余额
     *
     * @param userId 用户ID
     * @return 余额
     */
    @GetMapping("/inward/user/account/balance/{id}")
    ApiResult<Integer> balance(@PathVariable("id") String userId);

    /**
     * 账户余额支付
     *
     * @param request 账单信息
     * @return 支付结果
     */
    @PostMapping("/inward/user/account/payment")
    ApiResult<Boolean> payment(@RequestBody AccountPayRequest request);

    /**
     * 请求权限拦截
     *
     * @param userId  用户ID
     * @param version 版本号
     * @param uri     请求路径
     * @return 结果
     */
    @GetMapping("/inward/user/auth")
    ApiResult<Boolean> auth(@RequestParam("userId") String userId,
                            @RequestParam("version") Long version,
                            @RequestParam("uri") String uri);

    @GetMapping("/inward/user/data/scope")
    ApiResult<List<String>> getDataScope(@RequestParam("userId") String userId);

}
