package com.amusing.start.product.service.impl;

import com.amusing.start.product.mapper.ProductMessageInfoMapper;
import com.amusing.start.product.pojo.ProductMessageInfo;
import com.amusing.start.product.service.IProductMessageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create By 2021/11/14
 *
 * @author lvqingyu
 */
@Service
public class ProductMessageInfoServiceImpl implements IProductMessageInfoService {

    private final ProductMessageInfoMapper productMessageInfoMapper;

    @Autowired
    public ProductMessageInfoServiceImpl(ProductMessageInfoMapper productMessageInfoMapper) {
        this.productMessageInfoMapper = productMessageInfoMapper;
    }

    @Override
    public int save(ProductMessageInfo productMessageInfo) {
        return productMessageInfoMapper.insertSelective(productMessageInfo);
    }

    @Override
    public int updateStatus(String txId, Integer status, Integer resultStatus) {
        return productMessageInfoMapper.updateStatus(txId, status, resultStatus);
    }

}
