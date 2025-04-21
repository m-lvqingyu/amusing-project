package com.amusing.start.pay.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2024/8/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AliPayH5RefundRequest {
    /**
     * 退款金额，单位：元。支持两位小数
     */
    String refundAmount;
    /**
     * 订单编号
     */
    String orderNo;
    /**
     * 退款原因
     */
    String refundReason;
    /**
     * 退款编号。唯一
     */
    String outRequestNo;
    /**
     * 商品信息
     */
    private List<AliPayH5RefundGoodRequest> goodList;
}
