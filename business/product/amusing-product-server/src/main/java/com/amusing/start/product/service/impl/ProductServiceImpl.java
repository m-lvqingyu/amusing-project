package com.amusing.start.product.service.impl;

import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.code.CommCode;
import com.amusing.start.product.exception.ProductException;
import com.amusing.start.product.mapper.ProductInfoMapper;
import com.amusing.start.product.service.IProductService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@Slf4j
@Service
public class ProductServiceImpl implements IProductService {

    private ProductInfoMapper productInfoMapper;

    @Autowired
    public ProductServiceImpl(ProductInfoMapper productInfoMapper) {
        this.productInfoMapper = productInfoMapper;
    }

    @Override
    public boolean deductionStock(List<StockDeductionInput> inputs) throws ProductException {
        Integer result = null;
        try {
            result = productInfoMapper.batchDeductionStock(inputs);
        } catch (Exception e) {
            log.error("[product]-batchDeductionStock err! param:{}, msg:{}", inputs, Throwables.getStackTraceAsString(e));
        }
        Optional.ofNullable(result).filter(i -> i == inputs.size()).orElseThrow(() -> new ProductException(CommCode.FAIL));
        return true;
    }
}
