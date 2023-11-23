package com.amusing.start.client.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Lv.QingYu
 * @description: 支付宝交易订单详情
 * @since 2023/10/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AliPayTradeOutput {

    /**
     * 支付宝交易号
     */
    private String tradeNo;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 买家支付宝账号
     */
    private String buyerLogonId;

    /**
     * 交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
     * TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
     */
    private String tradeStatus;

    /**
     * 交易的订单金额。单位:分
     */
    private Integer totalAmount;

    /**
     * 买家实付金额。单位:分
     */
    private Integer buyerPayAmount;

    /**
     * 积分支付的金额，单位:分，两位小数。该金额代表该笔交易中用户使用积分支付的金额，比如集分宝或者支付宝实时优惠等
     */
    private Integer pointAmount;

    /**
     * 交易中用户支付的可开具发票的金额，单位:分，两位小数。该金额代表该笔交易中可以给用户开具发票的金额
     */
    private Integer invoiceAmount;

    /**
     * 本次交易打款给卖家的时间
     */
    private Date sendPayDate;

    /**
     * 实收金额，单位为分，两位小数。该金额为本笔交易，商户账户能够实际收到的金额
     */
    private Integer receiptAmount;

    /**
     * 请求交易支付中的商户店铺的名称
     */
    private String storeName;

    /**
     * 商户门店编号
     */
    private String storeId;

    /**
     * 商户机具终端编号
     */
    private String terminalId;

    /**
     * 买家用户类型。CORPORATE:企业用户；PRIVATE:个人用户
     */
    private String buyerUserType;

    /**
     * 商家优惠金额
     */
    private Integer midisCountAmount;

    /**
     * 平台优惠金额
     */
    private Integer discountAmount;

}
