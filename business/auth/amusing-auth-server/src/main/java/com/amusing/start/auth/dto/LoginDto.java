package com.amusing.start.auth.dto;

import lombok.Data;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
@Data
public class LoginDto {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 登录类型
     */
    private Integer loginType;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 设备号
     */
    private String imei;

}
