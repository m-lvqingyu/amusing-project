package com.amusing.start.order.enums;

/**
 * Create By 2021/11/13
 *
 * @author lvqingyu
 */
public enum MsgStatus {

    NOT_SEND(0, "未发送"),

    SEND(1, "已发送"),

    SEND_FAIL(2, "发送失败");

    private int key;

    private String value;

    MsgStatus(int key, String value){
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
