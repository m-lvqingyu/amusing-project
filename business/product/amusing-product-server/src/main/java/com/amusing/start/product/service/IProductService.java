package com.amusing.start.product.service;

import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.product.pojo.ProductInfo;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
public interface IProductService {

    /**
     * 获取商品详情
     *
     * @param shopId    商铺ID
     * @param productId 商品ID
     * @param priceId   价格ID
     * @return
     */
    ProductOutput getProductDetail(String shopId, String productId, String priceId);

    /**
     * 扣减库存
     *
     * @param shopId     商铺ID
     * @param productId  商品ID
     * @param productNum 商品数量
     * @return
     */
    boolean deductionProductStock(String txId, String shopId, String productId, Integer productNum);

    /**
     * 获取商品详情
     *
     * @param shopId    商铺ID
     * @param productId 商品ID
     * @return
     */
    ProductInfo getProductInfo(String shopId, String productId);

}
