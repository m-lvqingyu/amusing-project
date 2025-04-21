package com.amusing.start.order.service.impl;

import com.amusing.start.client.request.StockDeductionRequest;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.order.enums.OrderErrorCode;
import com.amusing.start.order.enums.ProductStatus;
import com.amusing.start.order.enums.YesNo;
import com.amusing.start.order.mapper.ProductMapper;
import com.amusing.start.order.pojo.Product;
import com.amusing.start.order.service.ProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2023/9/20
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    public Product getByName(String shopId, String name) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getShopId, shopId);
        wrapper.eq(Product::getName, name);
        wrapper.eq(Product::getIsDel, YesNo.NO.getKey());
        return productMapper.selectOne(wrapper);
    }

    @Override
    public void insert(Product product) {
        if (productMapper.insert(product) <= 0) {
            throw new CustomException(CommunalCode.SERVICE_ERR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deductionStock(List<StockDeductionRequest> requestList) {
        for (StockDeductionRequest request : requestList) {
            Product product = getById(request.getId());
            LambdaUpdateWrapper<Product> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Product::getId, request.getId());
            wrapper.ge(Product::getStock, 0);
            wrapper.eq(Product::getStatus, ProductStatus.VALID.getCode());
            wrapper.eq(Product::getIsDel, YesNo.NO.getKey());
            wrapper.set(Product::getStock, product.getStock() - 1);
            int update = productMapper.update(null, wrapper);
            if (update <= 0) {
                throw new CustomException(OrderErrorCode.PRODUCT_DEDUCTION_STOCK);
            }
        }
    }

    @Override
    public Product getById(String productId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getId, productId);
        wrapper.eq(Product::getIsDel, YesNo.NO.getKey());
        return productMapper.selectOne(wrapper);
    }

}
