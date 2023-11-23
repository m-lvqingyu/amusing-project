package com.amusing.start.platform.controller.outward.ali;

import com.amusing.start.platform.service.oauth.ali.AliPcOauthService;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lv.QingYu
 * @description: PC 网页内获取用户信息
 * @since 2023/11/10
 */
@Slf4j
@RestController
@RequestMapping(("platform/outward/ali"))
public class AliPcOauthController {

    private final AliPcOauthService aliPcOauthService;

    @Autowired
    public AliPcOauthController(AliPcOauthService aliPcOauthService) {
        this.aliPcOauthService = aliPcOauthService;
    }

    @GetMapping("oauth/call/back")
    public ApiResult<String> oauthCallBack(@RequestParam("app_id") String appId,
                                           @RequestParam("auth_code") String authCode) {
        return ApiResult.ok(aliPcOauthService.getUserId(appId, authCode));
    }

}
