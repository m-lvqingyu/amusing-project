package com.amusing.start.order.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @description: 订单是否使用优惠券
 * @since 2023/11/10
 */
@Getter
public enum OrderUseCouponEnum {

    YES(1, "使用"),

    NO(2, "未使用");

    private Integer key;

    private String value;

    OrderUseCouponEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

}
