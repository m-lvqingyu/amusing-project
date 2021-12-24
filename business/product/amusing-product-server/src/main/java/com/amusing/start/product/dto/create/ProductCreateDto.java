package com.amusing.start.product.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Administrator
 * @version 1.0
 * @date 2021/12/24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDto {

    /**
     * 商铺ID
     */
    private String shopId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品数量
     */
    private BigDecimal productStock;

    /**
     * 商品单价
     */
    private BigDecimal productPrice;

    /**
     * 描述
     */
    private String describe;

}
