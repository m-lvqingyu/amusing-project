package com.amusing.start.client.output.pay.ali;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/11/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BarCodePayOutput {

    /**
     * 支付宝交易号
     */
    private String tradeNo;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 买家支付宝账号
     */
    private String buyerLogonId;

    /**
     * 交易金额。单位：分
     */
    private Integer totalAmount;

    /**
     * 实收金额。单位：分
     */
    private Integer receiptAmount;

    /**
     * 买家付款的金额。单位：分
     */
    private Integer buyerPayAmount;

    /**
     * 使用集分宝付款的金额。单位：分
     */
    private Integer pointAmount;

    /**
     * 交易中可给用户开具发票的金额。单位：分
     */
    private Integer invoiceAmount;

    /**
     * 交易支付时间
     */
    private Date gmtPayment;

    /**
     * 商家优惠金额。单位：分
     */
    private Integer merchantDiscountAmount;

    /**
     * 平台优惠金额。单位：分
     */
    private Integer discountAmount;

}
