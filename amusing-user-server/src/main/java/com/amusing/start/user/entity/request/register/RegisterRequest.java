package com.amusing.start.user.entity.request.register;

import com.amusing.start.exception.CustomException;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.enums.UserErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Schema(description = "用户注册对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RegisterRequest {

    @Schema(description = "用户名")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9*@_]{4,31}$", message = "用户名格式不正确")
    private String userName;

    @Schema(description = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;

    @Schema(description = "密码")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9*@_]{5,11}$", message = "密码格式不正确")
    private String password;

    @Schema(description = "确认密码")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9*@_]{5,11}$", message = "密码格式不正确")
    private String confirmPassword;

    @Schema(description = "验证码")
    @NotBlank(message = "验证码为空")
    @Length(min = 4, max = 6, message = "验证码格式不正确")
    private String code;

    public void checkPs() {
        if (this.password.equals(this.confirmPassword)) {
            return;
        }
        throw new CustomException(UserErrorCode.PW_ERR);
    }

}
