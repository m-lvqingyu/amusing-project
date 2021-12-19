package com.amusing.start.product.service;

import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ShopOutput;
import com.amusing.start.product.exception.ProductException;

import java.util.List;
import java.util.Set;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
public interface IProductService {

    /**
     * 扣减库存
     *
     * @param inputs 商品信息
     * @return true: 成功 false: 失败
     * @throws ProductException
     */
    boolean deductionStock(List<StockDeductionInput> inputs) throws ProductException;

    /**
     * 根据商品ID，获取商品详情集合
     *
     * @param productIds 商品集合ID
     * @return
     */
    List<ShopOutput> getDetails(Set<String> productIds);

}
