package com.amusing.start.client.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/10/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AliPayAsyncNotifyInput {

    /**
     * 通知ID
     */
    String notifyId;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 支付宝交易流水号
     */
    private String tradeNo;
    /**
     * 买家支付宝账户
     */
    private String buyerLogonId;
    /**
     * 交易金额(单位：分)
     */
    private Integer totalAmount;
    /**
     * 实收金额(单位：分)
     */
    private Integer receiptAmount;
    /**
     * 买家付款金额(单位：分)
     */
    private Integer buyerPayAmount;
    /**
     * 使用集分宝付款的金额(单位：分)
     */
    private Integer pointAmount;
    /**
     * 交易中可给用户开具发票的金额
     */
    private Integer invoiceAmount;
    /**
     * 商家优惠金额(单位：分)
     */
    private Integer merchantDiscountAmount;
    /**
     * 平台优惠金额(单位：分)
     */
    private Integer discountAmount;
    /**
     * 交易时间
     */
    private String gmtPayment;

}
