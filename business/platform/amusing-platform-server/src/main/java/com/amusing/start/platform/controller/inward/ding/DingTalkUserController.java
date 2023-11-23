package com.amusing.start.platform.controller.inward.ding;

import com.amusing.start.client.api.DingTalkUserClient;
import com.amusing.start.client.output.DingTalkAccessToken;
import com.amusing.start.platform.service.ding.IDingTalkUserService;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lv.QingYu
 * @since 2023/10/3
 */
@Slf4j
@RestController
public class DingTalkUserController implements DingTalkUserClient {

    private final IDingTalkUserService dingTalkService;

    @Autowired
    public DingTalkUserController(IDingTalkUserService dingTalkService) {
        this.dingTalkService = dingTalkService;
    }

    @Override
    public ApiResult<DingTalkAccessToken> getAccessToken() {
        return ApiResult.ok(dingTalkService.getDingTalkAccessToken());
    }

    @Override
    public ApiResult<String> getUserId(String phone) {
        return ApiResult.ok(dingTalkService.getDingTalkUserId(phone));
    }

}
