package com.amusing.start.order.enums;

/**
 * Create By 2021/11/13
 *
 * @author lvqingyu
 */
public enum BusinessStatus {

    INIT(0, "初始化"),
    SUCCESS(1, "成功"),
    FAIL(2, "失败");

    private int key;
    private String value;

    BusinessStatus(int key, String value) {
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
