package com.amusing.start.user.controller.api.register;

import com.amusing.start.result.ApiResult;
import com.amusing.start.user.business.UserBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lv.QingYu
 * @since 2024/3/14
 */
@Tag(name = "用户注册-参数校验")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("user/api/register/check")
public class RegisterParamCheckController {

    private final UserBusiness userBusiness;

    @Operation(summary = "用户名是否被占用", description = "不需要登录")
    @Parameter(name = "name", description = "用户名")
    @GetMapping("name/{name}")
    public ApiResult<?> nameExist(@PathVariable("name") String name) {
        userBusiness.nameExist(name);
        return ApiResult.ok();
    }

    @Operation(summary = "手机号是否被占用", description = "不需要登录")
    @Parameter(name = "phone", description = "手机号")
    @GetMapping("phone/{phone}")
    public ApiResult<?> phoneExist(@PathVariable("phone") String phone) {
        userBusiness.phoneExist(phone);
        return ApiResult.ok();
    }

}
