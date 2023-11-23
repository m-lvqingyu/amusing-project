package com.amusing.start.order.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Lv.QingYu
 * @description: 订单表
 * @since 2021/10/10
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = -2102623645153358463L;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 预定人ID
     */
    private String reserveId;

    /**
     * 收件人ID
     */
    private String consigneeId;

    /**
     * 订单总金额
     */
    private Integer totalAmount;

    /**
     * 订单实际金额
     */
    private Integer realAmount;

    /**
     * 1:使用优惠券 2:未使用优惠券
     */
    private Integer useCoupon;

    /**
     * 优惠券减免总金额
     */
    private Integer couponAmount;

    /**
     * 1:参加活动 2:未参加活动
     */
    private Integer useActivity;

    /**
     * 活动减免总金额
     */
    private Integer activityAmount;

    /**
     * 1:包邮 2:不包邮
     */
    private Integer isFreight;

    /**
     * 运费
     */
    private Integer freightAmount;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 已评价
     */
    private Integer isEvaluate;

    /**
     * 支付类型 1:支付宝 2:微信
     */
    private Integer payType;

    /**
     * 支付ID
     */
    private Long payId;

    /**
     * 是否能够退款(1:可退款 2:不可退款)
     */
    private Integer canRefund;

    /**
     * 已退款金额
     */
    private Integer refundAmount;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

}