package com.amusing.start.product.pojo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 扣减库存消息信息
 * @date 2021/11/4 22:15
 */
@Data
@Builder
public class ReduceStockMsgInfo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 商铺ID
     */
    private String shopsId;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 商品数量
     */
    private Integer productNum;

    /**
     * 单价ID
     */
    private String priceId;

    /**
     * 订单总金额
     */
    private BigDecimal amount;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 消息状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
