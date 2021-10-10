package com.amusing.start.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Create By 2021/10/10
 *
 * @author lvqingyu
 */
@Data
public class OrderDetailVO {

    private String orderNo;

    /**
     * 预定人ID
     */
    private String reserveUserId;

    /**
     * 收件人ID
     */
    private String receivingUserId;

    /**
     * 运费
     */
    private BigDecimal freightAmount;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 优惠券减免总金额
     */
    private BigDecimal totalCouponAmount;

    /**
     * 活动减免总金额
     */
    private BigDecimal totalActivityAmount;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 是否包邮
     */
    private Integer freeFreight;

    /**
     * 已评价
     */
    private Integer isEvaluate;

    private List<OrderShopsVO> orderShopsVOList;

}
