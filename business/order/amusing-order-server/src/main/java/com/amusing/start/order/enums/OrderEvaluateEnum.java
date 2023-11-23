package com.amusing.start.order.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @description: 订单是否评价
 * @since 2023/11/10
 */
@Getter
public enum OrderEvaluateEnum {

    YES(1, "已评价"),

    NO(2, "未评价");

    private Integer key;

    private String value;

    OrderEvaluateEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

}
