package com.amusing.start.auth.enums;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
public enum UserType {

    ADMIN(1, "后台"),

    APP(2, "APP");

    private int key;

    private String value;

    UserType(int key, String value){
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
