package com.amusing.start.user.entity.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Lv.QingYu
 * @since 2024/10/24
 */
@Schema(description = "员工修改密码对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdminUserPwRequest {

    @Schema(description = "员工ID", example = "8273891831923")
    @NotBlank(message = "员工ID不能为空")
    private String userId;

    @Schema(description = "密码", example = "admin6379")
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9*@_]{5,11}$", message = "密码格式不正确")
    private String password;

    @Schema(description = "确认密码", example = "admin6379")
    @NotBlank(message = "确认密码不能为空")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9*@_]{5,11}$", message = "确认密码格式不正确")
    private String confirmPassword;

}
