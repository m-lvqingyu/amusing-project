package com.amusing.start.order.enums;

import com.amusing.start.code.ResultCode;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 订单Code码
 * @date 2021/10/15 22:43
 */
public enum OrderCode implements ResultCode<OrderCode> {

    ORDER_SAVE_FAIL(100, "订单创建失败"),

    ORDER_NOT_FOUND(101, "订单信息不存在"),

    USER_NOT_FOUND(102, "用户信息不存在"),

    PRODUCT_NOT_FOUND(103, "商品信息不存在");
    ;

    private final int code;
    private final String msg;

    OrderCode(final int code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public OrderCode get() {
        return this;
    }

    @Override
    public Integer key() {
        return code;
    }

    @Override
    public String value() {
        return msg;
    }
}
