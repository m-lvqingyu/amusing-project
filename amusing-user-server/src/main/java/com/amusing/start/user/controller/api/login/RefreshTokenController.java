package com.amusing.start.user.controller.api.login;

import com.amusing.start.result.ApiResult;
import com.amusing.start.user.business.LoginBusiness;
import com.amusing.start.user.entity.request.login.RefreshTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Lv.QingYu
 * @since 2024/3/14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/api/login")
public class RefreshTokenController {

    private final LoginBusiness loginBusiness;

    @PostMapping("/refresh")
    public ApiResult<String> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ApiResult.ok(loginBusiness.refresh(request.getToken()));
    }

}
