package com.amusing.start.order.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Create By 2021/10/10
 *
 * @author lvqingyu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductVO {

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
