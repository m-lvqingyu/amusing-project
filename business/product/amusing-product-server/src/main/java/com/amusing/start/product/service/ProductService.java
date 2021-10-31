package com.amusing.start.product.service;

import com.amusing.start.client.output.ProductOutput;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
public interface ProductService {

    /**
     * 获取商品详情
     *
     * @param shopId    商铺ID
     * @param productId 商品ID
     * @param priceId   价格ID
     * @return
     */
    ProductOutput getProductDetail(String shopId, String productId, String priceId);
}
