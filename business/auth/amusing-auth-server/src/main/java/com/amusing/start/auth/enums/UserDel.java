package com.amusing.start.auth.enums;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
public enum UserDel {

    NOT_DELETED(1, "未删除"),

    DELETED(2,"已删除");

    private int key;

    private String value;

    UserDel(int key, String value){
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
