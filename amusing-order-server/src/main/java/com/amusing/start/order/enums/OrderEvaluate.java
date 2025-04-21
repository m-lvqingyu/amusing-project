package com.amusing.start.order.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @since 2023/11/10
 */
@Getter
public enum OrderEvaluate {

    YES(1, "已评价"),

    NO(2, "未评价");

    private Integer key;

    private String value;

    OrderEvaluate(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

}
