package com.amusing.start.user.entity.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;

/**
 * @author Lv.QingYu
 * @since 2024/10/24
 */
@Schema(description = "员工修改对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdminUserEditRequest {

    @Schema(description = "员工ID", example = "189230131321232")
    @NotBlank(message = "用户名不能为空")
    private String userId;

    @Schema(description = "用户名", example = "张三")
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9*@_]{4,31}$", message = "用户名格式不正确")
    private String name;

    @Schema(description = "手机号码", example = "18888888888")
    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;

    @Schema(description = "状态(1:有效 2:冻结 3:无效)", example = "1")
    @NotNull(message = "请选择状态")
    @Min(value = 1, message = "状态不能小于1")
    @Max(value = 3, message = "状态不能超过3")
    private Integer status;
}
