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
    private BigDecimal price;

    /**
     * 商品数量
     */
    private BigDecimal productStock;

}
