package com.amusing.start.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Create By 2021/10/10
 *
 * @author lvqingyu
 */
@Data
public class OrderProductVO {

    /**
     * 商品名称
     */
    private String productId;

    /**
     * 单价
     */
    private BigDecimal productPrice;

    /**
     * 商品数量
     */
    private Integer productNum;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 优惠券ID
     */
    private String couponId;

    /**
     * 优惠券减免金额
     */
    private BigDecimal couponAmount;

    /**
     * 活动ID
     */
    private String activityId;

    /**
     * 活动减免金额
     */
    private BigDecimal activityAmount;

}
