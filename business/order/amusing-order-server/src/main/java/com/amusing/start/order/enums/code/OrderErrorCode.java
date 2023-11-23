package com.amusing.start.order.enums.code;

import com.amusing.start.code.BaseCode;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/10/16
 */
public enum OrderErrorCode implements BaseCode<OrderErrorCode> {

    ORDER_SAVE_FAIL("OR0000001", "订单创建失败"),

    ORDER_NOT_FOUND("OR0000002", "订单信息不存在"),

    ORDER_STATUS_UPDATE("OR0000003", "订单状态已发生改变"),

    ORDER_PAY_TIMEOUT("OR0000004", "订单已超时"),

    ORDER_NOT_REFUND("OR0000005", "订单已结束，无法退款"),

    ORDER_STATUS_REFUND("OR0000006", "订单状态错误，无法退款"),

    ORDER_USE_REFUND("OR0000007", "订单已全额退款"),

    ORDER_CLOSE("OR0000008", "订单已支付成功，无法关闭"),

    ACCOUNT_INSUFFICIENT("OA0000001", "用户账户金额不足"),

    ACCOUNT_NOT_FOUND("OA0000002", "用户账户信息不存在"),

    SHOP_CAR_ERROR("OS0000001", "请选择需要购买的商品"),


    ;


    private String key;

    private String value;

    OrderErrorCode(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public OrderErrorCode get() {
        return this;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
}
