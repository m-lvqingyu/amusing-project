package com.amusing.start.user.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "角色新增参数")
public class RoleAddDto {

    @ApiModelProperty(value = "角色ID")
    private Integer id;

    @NotEmpty(message = "角色名称不能为空")
    @ApiModelProperty(value = "名称")
    private String name;

    @NotEmpty(message = "角色编码不能为空")
    @ApiModelProperty(value = "编码")
    private String nameCode;

    @ApiModelProperty(value = "描述")
    private String description;

    @NotNull(message = "角色状态不能为空")
    @Min(value = 1, message = "角色状态错误")
    @Max(value = 3, message = "角色状态错误")
    @ApiModelProperty(value = "状态(1:有效 2:冻结 3:无效)")
    private Integer status;

}
