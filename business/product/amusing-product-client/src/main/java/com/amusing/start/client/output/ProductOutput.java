package com.amusing.start.client.output;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@Data
public class ProductOutput {

    /**
     * 商铺ID
     */
    private String shopId;

    /**
     * 商铺名称
     */
    private String shopName;

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
     * 活动ID
     */
    private String activityId;

    /**
     * 商品单价
     */
    private BigDecimal price;

    /**
     * 商品数量
     */
    private BigDecimal productStock;

    /**
     * 状态
     */
    private Integer status;

}
