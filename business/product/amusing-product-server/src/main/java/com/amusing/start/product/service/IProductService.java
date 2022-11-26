package com.amusing.start.product.service;

import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ShopCarOutput;
import com.amusing.start.exception.CustomException;
import com.amusing.start.product.entity.dto.ProductCreateDto;

import java.util.List;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
public interface IProductService {

    /**
     * 商品新增
     *
     * @param executor  执行者ID
     * @param createDto 商品信息
     * @return 商品ID
     */
    String create(String executor, ProductCreateDto createDto) throws CustomException;

    /**
     * 购物车列表
     *
     * @param userId 用户ID
     * @return 购物车列表
     */
    List<ShopCarOutput> shopCar(String userId) throws CustomException;

    /**
     * 扣减库存
     *
     * @param inputs 商品信息
     * @return true: 成功 false: 失败
     */
    Boolean deductionStock(List<StockDeductionInput> inputs) throws CustomException;


}
