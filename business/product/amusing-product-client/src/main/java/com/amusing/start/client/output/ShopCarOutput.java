package com.amusing.start.client.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ShopCarOutput {

    /**
     * 商铺ID
     */
    private String shopId;

    /**
     * 商铺名称
     */
    private String shopName;

    /**
     * 商铺顺序
     */
    private Integer sort;

    /**
     * 商品信息
     */
    private List<ShopCarProductOutput> productOutputList;

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShopCarProductOutput {

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
        private Integer price;

        /**
         * 商品数量
         */
        private Integer stock;
    }


}
