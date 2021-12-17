package com.amusing.start.order.pojo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author
 */
@Data
@Builder
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
     * 商品名称
     */
    private String productName;

    /**
     * 价格ID
     */
    private String priceId;

    /**
     * 商品单价
     */
    private String productPrice;

    /**
     * 商品数量
     */
    private Integer productNum;

    /**
     * 商品金额
     */
    private BigDecimal amount;

}