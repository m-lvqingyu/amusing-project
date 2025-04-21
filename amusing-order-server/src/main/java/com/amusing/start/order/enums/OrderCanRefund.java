package com.amusing.start.order.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @since 2023/11/10
 */
@Getter
public enum OrderCanRefund {

    YES(1, "可退款"),

    NO(2, "不可退款");

    private final Integer key;

    private final String value;

    OrderCanRefund(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

}
