package com.amusing.start.order.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/9/26
 */
@Getter
public enum OrderPaySubTypeEnum {

    PC(1, "电脑网站"),

    BAR_CODE(2, "付款码支付"),

    PRE_SCAN_CODE(3, "扫码支付"),

    SCAN_CODE(4, "扫码支付-一码多扫");

    private int key;

    private String desc;

    OrderPaySubTypeEnum(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }

}
