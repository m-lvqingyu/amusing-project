package com.amusing.start.product.service.impl;

import com.amusing.start.product.entity.pojo.ProductPriceInfo;
import com.amusing.start.product.mapper.ProductPriceInfoMapper;
import com.amusing.start.product.service.IProductPriceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Lv.QingYu
 * @description: 商品价格Service
 * @since 2023/9/21
 */
@Service
public class ProductPriceServiceImpl implements IProductPriceService {

    @Resource
    private ProductPriceInfoMapper productPriceInfoMapper;

    @Override
    public Integer insert(ProductPriceInfo productPriceInfo) {
        return productPriceInfoMapper.insert(productPriceInfo);
    }

    @Override
    public ProductPriceInfo getLastPrice(String productId) {
        return productPriceInfoMapper.getLastPrice(productId);
    }

}
