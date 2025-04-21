package com.amusing.start.order.service.impl;

import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.order.mapper.PriceMapper;
import com.amusing.start.order.pojo.Price;
import com.amusing.start.order.service.PriceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Lv.QingYu
 * @since 2023/9/21
 */
@Service
public class PriceServiceImpl implements PriceService {

    @Resource
    private PriceMapper productPriceMapper;

    private final static String LAST_SQL = " limit 1";

    @Override
    public void insert(Price productPrice) {
        if (productPriceMapper.insert(productPrice) <= 0) {
            throw new CustomException(CommunalCode.SERVICE_ERR);
        }
    }

    @Override
    public Price getLastPrice(String productId) {
        LambdaQueryWrapper<Price> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Price::getProductId, productId);
        wrapper.orderByDesc(Price::getVersion);
        wrapper.last(LAST_SQL);
        return productPriceMapper.selectOne(wrapper);
    }

}
