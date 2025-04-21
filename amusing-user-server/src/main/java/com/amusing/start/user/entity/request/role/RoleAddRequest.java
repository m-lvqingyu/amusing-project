package com.amusing.start.user.entity.request.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Lv.QingYu
 * @description: 角色新增参数
 * @since 2024/8/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleAddRequest {

    private Integer id;

    @NotEmpty(message = "角色名称不能为空")
    private String name;

    @NotEmpty(message = "角色编码不能为空")
    private String code;

    private String description;

    @NotNull(message = "角色状态不能为空")
    @Min(value = 1, message = "角色状态错误")
    @Max(value = 3, message = "角色状态错误")
    private Integer status;

}
