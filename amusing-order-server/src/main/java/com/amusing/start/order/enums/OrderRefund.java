package com.amusing.start.order.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @since 2023/10/2
 */
@Getter
public enum OrderRefund {

    INIT(1, "初始状态"),

    SUCCESS(5, "成功"),

    FAIL(10, "失败");

    private final int key;

    private final String desc;

    OrderRefund(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
