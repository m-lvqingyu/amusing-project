package com.amusing.start.client.api;

import com.amusing.start.client.fallback.AuthClientFallback;
import com.amusing.start.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lv.qingyu
 */
@FeignClient(value = "amusing-auth-server", fallbackFactory = AuthClientFallback.class)
public interface AuthClient {

    /**
     * 判断用户是否有效
     *
     * @param userId 用户ID
     * @return true:有效 false:无效
     */
    @GetMapping("/auth/inward/v1/valid/{userId}")
    ApiResult<Boolean> isValid(@PathVariable("userId") String userId);

}
