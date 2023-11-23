package com.amusing.start.product.service;

import com.amusing.start.product.entity.pojo.ProductPriceInfo;

/**
 * @author Lv.QingYu
 * @description: 商品价格Service
 * @since 2023/9/21
 */
public interface IProductPriceService {

    Integer insert(ProductPriceInfo productPriceInfo);

    ProductPriceInfo getLastPrice(String productId);


}
