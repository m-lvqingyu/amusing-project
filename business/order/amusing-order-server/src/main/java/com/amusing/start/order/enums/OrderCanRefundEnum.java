package com.amusing.start.order.enums;

import lombok.Getter;

@Getter
public enum OrderCanRefundEnum {

    YES(1, "可退款"),

    NO(2, "不可退款");

    private Integer key;

    private String value;

    OrderCanRefundEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

}
