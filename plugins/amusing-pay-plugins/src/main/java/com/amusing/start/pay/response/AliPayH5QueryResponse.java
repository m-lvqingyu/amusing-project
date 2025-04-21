package com.amusing.start.pay.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2024/8/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AliPayH5QueryResponse {
    /**
     * 支付宝交易号
     */
    private String aliOrderNo;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 买家支付宝账号
     */
    private String buyerLogonId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 订单金额。单位为元，两位小数
     */
    private String totalAmount;
    /**
     * 下单时商品信息列表
     */
    private List<AliPayH5QueryGoodResponse> initGoodResponse;
    /**
     * 述当前账期交易的场景
     */
    private String periodScene;
    /**
     * 标价币种
     */
    private String transCurrency;
    /**
     * 订单结算币种
     */
    private String settleCurrency;
    /**
     * 结算币种订单金额
     */
    private String settleAmount;
    /**
     * 订单支付币种
     */
    private String payCurrency;
    /**
     * 支付币种订单金额
     */
    private String payAmount;
    /**
     * 结算币种兑换标价币种汇率
     */
    private String settleTransRate;
    /**
     * 标价币种兑换支付币种汇率
     */
    private String transPayRate;
    /**
     * 买家实付金额，单位为元，两位小数。该金额代表该笔交易买家实际支付的金额，不包含商户折扣等金额
     */
    private String buyerPayAmount;
    /**
     * 积分支付的金额，单位为元，两位小数。该金额代表该笔交易中用户使用积分支付的金额，比如集分宝或者支付宝实时优惠等
     */
    private String pointAmount;
    /**
     * 交易中用户支付的可开具发票的金额，单位为元，两位小数。该金额代表该笔交易中可以给用户开具发票的金额
     */
    private String invoiceAmount;
    /**
     * 本次交易打款给卖家的时间
     */
    private Date sendPayDate;
    /**
     * 实收金额，单位为元，两位小数。该金额为本笔交易，商户账户能够实际收到的金额
     */
    private String receiptAmount;
    /**
     * 买家支付宝用户唯一标识
     */
    private String buyerOpenId;
}
