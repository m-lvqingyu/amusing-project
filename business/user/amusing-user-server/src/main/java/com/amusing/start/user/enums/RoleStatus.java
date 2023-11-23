package com.amusing.start.user.enums;

public enum RoleStatus {

    VALID(1, "有效"),
    
    INVALID(3, "无效");

    private int key;

    private String value;

    RoleStatus(int key, String value) {
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
