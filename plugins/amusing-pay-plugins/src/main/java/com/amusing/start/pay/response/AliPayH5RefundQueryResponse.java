package com.amusing.start.pay.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @since 2024/8/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AliPayH5RefundQueryResponse {
    /**
     * 支付宝交易号
     */
    private String aliOrderNo;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 退款编号
     */
    private String outRequestNo;
    /**
     * 订单金额。单位：元
     */
    private String totalAmount;
    /**
     * 退款金额。单位：元
     */
    private String refundAmount;
    /**
     * 退款状态。1：成功
     */
    private Integer status;
}
