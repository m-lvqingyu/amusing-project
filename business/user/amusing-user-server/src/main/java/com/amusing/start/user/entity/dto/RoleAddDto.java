package com.amusing.start.user.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by 2023/04/11.
 *
 * @author lvqingyu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleAddDto {

    private Integer id;

    /**
     * 角色名称
     */
    @NotEmpty(message = "角色名称不能为空")
    private String name;

    /**
     * 名称码
     */
    @NotEmpty(message = "角色编码不能为空")
    private String nameCode;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 状态(1:有效 2:冻结 3:无效)
     */
    @NotNull(message = "角色状态不能为空")
    @Min(value = 1, message = "角色状态错误")
    @Max(value = 3, message = "角色状态错误")
    private Integer status;

}
