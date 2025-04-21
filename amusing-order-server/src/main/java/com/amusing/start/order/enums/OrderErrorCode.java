package com.amusing.start.order.enums;

import com.amusing.start.code.BaseCode;

/**
 * @author Lv.QingYu
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

    PRODUCT_CREATE_ERR("P00000001", "商品新增失败"),

    PRODUCT_NAME_EXIST("P00000002", "商品名称已经存在"),

    PRODUCT_PRICE_ERR("P00000003", "商品单价需大于零"),

    PRODUCT_DEDUCTION_STOCK("P00000004", "商品库存更新失败"),

    PRODUCT_NOT_FOUND("P00000005", "商品不存在或已下架"),


    SHOP_NOT_FOUND("P00000005", "商铺不存在或已被删除"),

    SHOP_CREATE_ERR("P00000006", "商铺新增失败"),

    SHOP_NAME_EXIST("P00000007", "商铺名称已经存在"),

    ;

    private final String key;

    private final String value;

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
