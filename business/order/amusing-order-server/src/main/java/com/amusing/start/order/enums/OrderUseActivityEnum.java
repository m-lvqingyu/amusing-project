package com.amusing.start.order.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @description: 订单是否参加活动
 * @since 2023/11/10
 */
@Getter
public enum OrderUseActivityEnum {

    YES(1, "参加"),

    NO(2, "未参加");

    private Integer key;

    private String value;

    OrderUseActivityEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

}
