package com.amusing.start.auth.from;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
@Data
public class LoginFrom {

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    private String password;

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 登录类型
     */
    @NotNull(message = "请求参数不合法!")
    private Integer loginType;

    /**
     * 用户类型
     */
    @NotNull(message = "请求参数不合法!")
    private Integer userType;
}
