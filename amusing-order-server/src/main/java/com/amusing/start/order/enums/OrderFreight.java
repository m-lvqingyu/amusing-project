package com.amusing.start.order.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @since 2023/11/10
 */
@Getter
public enum OrderFreight {

    YES(1, "包邮"),

    NO(2, "不包邮");

    private final Integer key;

    private final String value;

    OrderFreight(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

}
