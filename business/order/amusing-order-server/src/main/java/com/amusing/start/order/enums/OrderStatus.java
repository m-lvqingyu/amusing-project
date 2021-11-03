package com.amusing.start.order.enums;

/**
 * Create By 2021/10/30
 *
 * @author lvqingyu
 */
public enum OrderStatus {

    SCHEDULED(10, "预定中"),

    REDUCE_STOCK(20, "扣减库存"),

    IN_PAYMENT(30, "支付中"),

    ALREADY_PAID(40, "已支付"),

    CANCEL(100, "已取消"),
    ;

    private int key;
    private String value;

    OrderStatus(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
