package com.amusing.start.user.controller.api.register;

import com.amusing.start.result.ApiResult;
import com.amusing.start.user.business.UserBusiness;
import com.amusing.start.user.entity.request.register.RegisterRequest;
import com.amusing.start.user.enums.RegisterType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Lv.QingYu
 * @since 2024/3/14
 */
@Tag(name = "用户注册")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("user/api/register")
public class RegisterController {

    private final UserBusiness userBusiness;

    @Operation(summary = "用户注册", description = "不需要登录")
    @PostMapping
    public ApiResult<?> register(@Valid @RequestBody RegisterRequest request) {
        request.checkPs();
        String phone = request.getPhone();
        String userName = request.getUserName();
        String password = request.getPassword();
        String code = request.getCode();
        userBusiness.register(RegisterType.APP, phone, userName, password, code);
        return ApiResult.ok();
    }

}
