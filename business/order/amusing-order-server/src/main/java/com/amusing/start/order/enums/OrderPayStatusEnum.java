package com.amusing.start.order.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/9/26
 */
@Getter
public enum OrderPayStatusEnum {

    SUCCESS(10, "支付成功"),

    CLOSE_REFUND(20, "退款关闭"),
    ;

    private int key;

    private String desc;

    OrderPayStatusEnum(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }

}
