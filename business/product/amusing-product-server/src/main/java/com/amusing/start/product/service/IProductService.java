package com.amusing.start.product.service;

import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ShopCarOutput;
import com.amusing.start.exception.CustomException;
import com.amusing.start.product.entity.dto.ProductCreateDto;
import com.amusing.start.product.entity.pojo.ProductInfo;

import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 商品服务Service
 * @since 2023/9/20
 */
public interface IProductService {

    /**
     * @param shopId 商铺ID
     * @param name   商品名称
     * @return 商品信息
     * @description: 根据名称获取商品信息
     */
    ProductInfo getByName(String shopId, String name);

    Integer insert(ProductInfo productInfo);

    ProductInfo getById(String productId);

    Integer batchDeductionStock(List<StockDeductionInput> inputs);

}
