package com.amusing.start.pay.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Lv.QingYu
 * @since 2024/8/8
 */
@Getter
public enum AliPayRefundStatus {

    REFUND_SUCCESS(1, "REFUND_SUCCESS"),

    UN_KNOW(2, "UN_KNOW");

    private int key;

    private String value;

    AliPayRefundStatus(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static AliPayRefundStatus getByValue(String value) {
        if (StringUtils.isBlank(value)) {
            return AliPayRefundStatus.UN_KNOW;
        }
        AliPayRefundStatus[] values = AliPayRefundStatus.values();
        for (AliPayRefundStatus refundStatus : values) {
            if (refundStatus.getValue().equalsIgnoreCase(value)) {
                return refundStatus;
            }
        }
        return AliPayRefundStatus.UN_KNOW;
    }

}
