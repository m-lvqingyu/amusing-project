package com.amusing.start.order.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author
 */
@Data
@Builder
public class OrderMessageInfo {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 事务ID
     */
    private String transactionId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 消息发送状态（0：未发送 1：已发送 3：发送失败）
     */
    private Integer msgStatus;

    /**
     * 商铺服务状态
     */
    private Integer productBusinessStatus;

    /**
     * 用户服务状态
     */
    private Integer userBusinessStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}