package com.amusing.start.product.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lvqingyu on 2022/10/15.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopCarDto implements Serializable {

    private static final long serialVersionUID = -1903949654400438129L;

    /**
     * 商铺ID
     */
    private String shopId;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 商品列表
     */
    private List<ProductDto> productList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductDto implements Serializable {

        private static final long serialVersionUID = 1913589464413549691L;

        /**
         * 商品ID
         */
        private String productId;

        /**
         * 商品数量
         */
        private Integer num;

        /**
         * 顺序
         */
        private Integer sort;

    }


}
