package com.amusing.start.order.service;

import com.amusing.start.client.request.StockDeductionRequest;
import com.amusing.start.order.pojo.Product;

import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2023/9/20
 */
public interface ProductService {

    /**
     * 根据商品名称获取商品信息
     *
     * @param shopId 商铺ID
     * @param name   商品名称
     * @return 商品信息
     */
    Product getByName(String shopId, String name);

    /**
     * 保存商品信息
     *
     * @param product 商品信息
     */
    void insert(Product product);

    /**
     * 扣减库存
     *
     * @param requestList 商品数量信息
     */
    void deductionStock(List<StockDeductionRequest> requestList);

    /**
     * 更具ID获取商品信息
     *
     * @param productId 商品ID
     * @return 商品信息
     */
    Product getById(String productId);

}
