package com.amusing.start.order.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @since 2023/9/26
 */
@Getter
public enum OrderPayType {

    UNKNOWN(0, "未知"),

    ALI(1, "支付宝"),

    WE_CHAT(2, "微信");

    private final int key;

    private final String desc;

    OrderPayType(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
