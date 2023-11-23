package com.amusing.start.product.service.impl;

import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.product.mapper.ProductInfoMapper;
import com.amusing.start.product.entity.pojo.ProductInfo;
import com.amusing.start.product.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Lv.QingYu
 * @description: 商品服务ServiceImpl
 * @since 2023/9/20
 */
@Slf4j
@Service
public class ProductServiceImpl implements IProductService {

    @Resource
    private ProductInfoMapper productInfoMapper;

    @Override
    public ProductInfo getByName(String shopId, String name) {
        return productInfoMapper.selectByName(shopId, name);
    }

    @Override
    public Integer insert(ProductInfo productInfo) {
        return productInfoMapper.insert(productInfo);
    }

    @Override
    public ProductInfo getById(String productId) {
        return productInfoMapper.getById(productId);
    }

    @Override
    public Integer batchDeductionStock(List<StockDeductionInput> inputs) {
        return productInfoMapper.batchDeductionStock(inputs);
    }

}
