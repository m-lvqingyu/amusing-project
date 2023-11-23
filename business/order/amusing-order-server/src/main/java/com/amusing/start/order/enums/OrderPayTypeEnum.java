package com.amusing.start.order.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/9/26
 */
@Getter
public enum OrderPayTypeEnum {

    ALI(1, "支付宝"),

    WE_CHAT(2, "微信");

    private int key;

    private String desc;

    OrderPayTypeEnum(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
