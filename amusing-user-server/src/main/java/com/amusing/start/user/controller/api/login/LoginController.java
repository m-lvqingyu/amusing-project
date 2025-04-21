package com.amusing.start.user.controller.api.login;

import com.amusing.start.result.ApiResult;
import com.amusing.start.user.business.LoginBusiness;
import com.amusing.start.user.entity.request.login.LoginRequest;
import com.amusing.start.user.entity.response.TokenResponse;
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
public class LoginController {

    private final LoginBusiness loginBusiness;

    @PostMapping
    public ApiResult<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResult.ok(loginBusiness.login(request));
    }

}
