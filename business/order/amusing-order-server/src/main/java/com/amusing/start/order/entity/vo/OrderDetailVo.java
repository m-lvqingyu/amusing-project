package com.amusing.start.order.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Create By 2021/10/10
 *
 * @author lvqingyu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVo {

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
    private BigDecimal totalAmount;

    /**
     * 运费
     */
    private BigDecimal freightAmount;

    /**
     * 优惠券减免总金额
     */
    private BigDecimal couponAmount;

    /**
     * 活动减免总金额
     */
    private BigDecimal activityAmount;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 是否包邮
     */
    private Integer isFreight;

    /**
     * 已评价
     */
    private Integer isEvaluate;

    private List<OrderProductVo> productList;

}
