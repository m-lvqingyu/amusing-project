package com.amusing.start.client.api;

import com.amusing.start.client.fallback.DingTalkUserClientFallback;
import com.amusing.start.client.output.DingTalkAccessToken;
import com.amusing.start.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lv.QingYu
 * @description: 钉钉用户相关接口
 * @since 2023/10/3
 */
@FeignClient(
        name = "amusing-platform-server",
        fallbackFactory = DingTalkUserClientFallback.class,
        contextId = "amusing-platform-server-DingTalkUserClient"
)
public interface DingTalkUserClient {

    /**
     * @return 企业内部Access_Token
     * @description: 获取企业内部Access_Token
     */
    @GetMapping("platform/in/ding/talk/access/token")
    ApiResult<DingTalkAccessToken> getAccessToken();

    /**
     * @param phone 手机号码
     * @return 用户ID
     * @description: 根据手机号码，获取该手机号对应的用户ID
     */
    @GetMapping("platform/in/ding/talk/user/id")
    ApiResult<String> getUserId(@RequestParam("phone") String phone);

}
