package com.amusing.start.auth.controller.inward;

import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.auth.service.IUserService;
import com.amusing.start.client.api.AuthClient;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lv.qingyu
 */
@RestController
public class AuthInwardController implements AuthClient {

    @Autowired
    private IUserService userService;

    @Override
    public ApiResult<Boolean> isValid(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
        }
        try {
            boolean result = userService.checkUserValid(userId);
            return ApiResult.ok(result);
        } catch (AuthException e) {
            return ApiResult.result(e.getAuthCode());
        }
    }

}
