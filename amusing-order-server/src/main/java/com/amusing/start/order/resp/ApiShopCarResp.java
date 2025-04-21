package com.amusing.start.order.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lvqingyu on 2022/10/15.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ApiShopCarResp implements Serializable {
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
    private List<ApiProductResp> productList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class ApiProductResp implements Serializable {
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
