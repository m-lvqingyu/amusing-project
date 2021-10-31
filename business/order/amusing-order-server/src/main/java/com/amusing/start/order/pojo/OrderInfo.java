package com.amusing.start.order.pojo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author
 */
@Data
@Builder
public class OrderInfo {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 预定人ID
     */
    private String reserveUserId;

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

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

}