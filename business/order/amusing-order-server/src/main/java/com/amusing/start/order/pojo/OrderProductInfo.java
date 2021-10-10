package com.amusing.start.order.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class OrderProductInfo {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 商铺ID
     */
    private String shopsId;

    /**
     * 商品ID
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