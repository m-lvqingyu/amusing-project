package com.amusing.start.order.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/10/2
 */
@Getter
public enum OrderRefundEnum {

    INIT(1, "初始状态"),

    SUCCESS(5, "成功"),

    FAIL(10, "失败");

    private int key;

    private String desc;

    OrderRefundEnum(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
