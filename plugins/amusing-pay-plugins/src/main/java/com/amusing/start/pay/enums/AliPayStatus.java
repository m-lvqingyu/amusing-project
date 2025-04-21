package com.amusing.start.pay.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Lv.QingYu
 * @since 2024/8/8
 */
@Getter
public enum AliPayStatus {

    WAIT_BUYER_PAY(1, "WAIT_BUYER_PAY", "交易创建，等待买家付款"),

    TRADE_CLOSED(5, "TRADE_CLOSED", "未付款交易超时关闭，或支付完成后全额退款"),

    TRADE_SUCCESS(10, "TRADE_SUCCESS", "交易支付成功"),

    TRADE_FINISHED(15, "TRADE_FINISHED", "交易结束，不可退款"),

    UN_KNOW(20, "UN_KNOW", "未知");

    private final int key;

    private final String value;

    private final String desc;

    AliPayStatus(int key, String value, String desc) {
        this.key = key;
        this.value = value;
        this.desc = desc;
    }

    public static AliPayStatus getByValue(String value) {
        if (StringUtils.isBlank(value)) {
            return AliPayStatus.UN_KNOW;
        }
        AliPayStatus[] values = AliPayStatus.values();
        for (AliPayStatus status : values) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return AliPayStatus.UN_KNOW;
    }

}
