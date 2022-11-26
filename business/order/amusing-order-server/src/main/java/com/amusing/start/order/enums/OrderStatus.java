package com.amusing.start.order.enums;

/**
 * Create By 2021/10/30
 *
 * @author lvqingyu
 */
public enum OrderStatus {

    SCHEDULED(10, "预定中"),

    REDUCE_STOCK(20, "扣减库存"),

    PAYMENT(30, "支付中"),

    PAY(40, "已支付"),

    CANCEL(100, "已取消");

    private int key;

    private String desc;

    OrderStatus(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public int getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }
}
