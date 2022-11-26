package com.amusing.start.user.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by lvqingyu on 2022/10/2.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDto {

    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotNull(message = "注册来源不能为空")
    private Integer sources;

    @NotEmpty(message = "手机号码不能为空")
    private String phone;

}
