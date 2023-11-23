package com.amusing.start.order.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @description: 订单是否包邮
 * @since 2023/11/10
 */
@Getter
public enum OrderFreightEnum {

    YES(1, "包邮"),

    NO(2, "不包邮");

    private Integer key;

    private String value;

    OrderFreightEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

}
