package com.amusing.start.user.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(description = "注册请求参数")
public class RegisterDto {

    @ApiModelProperty(value = "用户名")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9*@_]{4,31}$", message = "用户名格式不正确")
    private String userName;

    @ApiModelProperty(value = "密码")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9*@_]{5,11}$", message = "密码格式不正确")
    private String password;

    @ApiModelProperty(value = "手机号码")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;

}
