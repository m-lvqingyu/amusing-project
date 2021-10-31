package com.amusing.start.order.enums;

/**
 * Create By 2021/10/30
 *
 * @author lvqingyu
 */
public enum YesNo {

    YES(1, "是"),

    NO(2, "否"),
    ;

    private int key;
    private String value;

    YesNo(int key, String value) {
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
