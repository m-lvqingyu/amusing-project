package com.amusing.start.order.entity.vo;

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
public class OrderProductVo {

    /**
     * 订单编号
     */
    private String orderNo;

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
     * 单价ID
     */
    private String priceId;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 商品数量
     */
    private Integer num;

    /**
     * 订单金额
     */
    private BigDecimal amount;

}
