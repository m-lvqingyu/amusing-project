package com.amusing.start.product.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 商品价格信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductPriceInfo {

    /**
     * 价格ID
     */
    private String priceId;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Long updateTime;

}