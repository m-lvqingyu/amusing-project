package com.amusing.start.order.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 订单信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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