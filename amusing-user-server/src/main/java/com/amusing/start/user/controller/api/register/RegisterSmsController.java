package com.amusing.start.user.controller.api.register;

import com.amusing.start.result.ApiResult;
import com.amusing.start.user.constant.NumberConstant;
import com.amusing.start.user.entity.request.register.RegisterSmsRequest;
import com.amusing.start.user.service.SmsService;
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
@Tag(name = "用户注册-验证码发送")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("user/api/register")
public class RegisterSmsController {

    private final SmsService smsService;

    @Operation(summary = "注册时短信验证码", description = "不需要登录")
    @PostMapping("sms")
    public ApiResult<?> sms(@Valid @RequestBody RegisterSmsRequest request) {
        String code = String.valueOf(NumberConstant.generateSixFiguresNum());
        smsService.sendRegisterCode(request.getPhone(), code);
        return ApiResult.ok();
    }

}
