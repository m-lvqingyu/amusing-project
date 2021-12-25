package com.amusing.start.product.service;

import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.client.output.ShopOutput;
import com.amusing.start.product.dto.create.ProductCreateDto;
import com.amusing.start.product.exception.ProductException;

import java.util.List;
import java.util.Map;
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
     * 商品新增
     *
     * @param executor  执行者ID
     * @param createDto 商品信息
     * @return 商品ID
     * @throws ProductException
     */
    String create(String executor, ProductCreateDto createDto) throws ProductException;

    /**
     * 获取商品详细
     *
     * @param productIds 商品ID集合
     * @return
     */
    Map<String, ProductOutput> getProductDetails(Set<String> productIds);

    /**
     * 获取商铺详情
     *
     * @param shopIds 商铺ID集合
     * @return
     */
    Map<String, ShopOutput> getShopDetails(Set<String> shopIds);

}
