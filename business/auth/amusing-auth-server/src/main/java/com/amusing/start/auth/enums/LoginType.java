package com.amusing.start.auth.enums;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
public enum LoginType {

    PHONE(1, "手机号验证码登录"),

    USERNAME(2, "用户名密码登录");

    private int key;

    private String value;

    LoginType(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
