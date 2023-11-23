package com.amusing.start.user.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "登录请求参数")
public class LoginDto {

    @ApiModelProperty(value = "用户名")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9*@_]{4,31}$", message = "用户名格式不正确")
    private String userName;

    @ApiModelProperty(value = "密码")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9*@_]{5,11}$", message = "密码格式不正确")
    private String password;

}
