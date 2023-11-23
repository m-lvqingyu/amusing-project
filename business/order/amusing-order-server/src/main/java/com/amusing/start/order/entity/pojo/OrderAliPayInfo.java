package com.amusing.start.order.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @description: 订单支付信息
 * @since 2023/9/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderAliPayInfo {
    /**
     * 主键
     */
    private Long id;
    /**
     * 消息ID
     */
    private String notifyId;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 支付宝交易号
     */
    private String tradeNo;
    /**
     * 买家支付宝账户
     */
    private String buyerLogonId;
    /**
     * 1:PC网站支付 2:付款码支付 3:扫码支付 4:扫码支付-一码多扫
     */
    private Integer type;
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
    /**
     * 支付状态：10-支付成功 20-退款关闭
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 更新时间
     */
    private Long updateTime;
}
