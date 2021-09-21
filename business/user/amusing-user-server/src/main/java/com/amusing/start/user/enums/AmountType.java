package com.amusing.start.user.enums;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
public enum AmountType {

    MAIN(1, "主账户"),

    GIVE(2, "副账户"),

    MIX(3, "混合");

    private int key;

    private String value;

    AmountType(int key, String value) {
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
