package com.amusing.start.order.enums;

/**
 * @author Lv.QingYu
 * @description: 订单状态枚举
 * @since 2021/10/30
 */
public enum OrderStatusEnum {

    SCHEDULED(10, "预定中"),

    PAY(20, "已支付"),

    PART_REFUND(30, "部分退款-部分"),

    PART_REFUND_FINISH(35, "部分退款-全额"),

    REFUND(40, "全额退款"),

    CLOSE_TIMEOUT(90, "超时关闭"),

    CANCEL(100, "已取消");

    private int key;

    private String desc;

    OrderStatusEnum(int key, String desc) {
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
