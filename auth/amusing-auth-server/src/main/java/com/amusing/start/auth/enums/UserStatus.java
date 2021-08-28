package com.amusing.start.auth.enums;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
public enum UserStatus {

    VALID(1, "有效"),

    FROZEN(2, "冻结"),

    INVALID(3, "无效");

    private int key;

    private String value;

    UserStatus(int key, String value){
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
