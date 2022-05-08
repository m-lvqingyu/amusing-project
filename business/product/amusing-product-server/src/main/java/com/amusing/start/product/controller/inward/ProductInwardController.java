package com.amusing.start.product.controller.inward;

import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.product.constant.ProductConstant;
import com.amusing.start.product.exception.ProductException;
import com.amusing.start.product.service.IProductService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@Slf4j
@RestController
public class ProductInwardController implements ProductClient {

    private final IProductService productService;

    @Autowired
    public ProductInwardController(IProductService productService) {
        this.productService = productService;
    }

    @Override
    public Boolean deductionStock(List<StockDeductionInput> inputs) {
        if (CollectionUtil.isEmpty(inputs)) {
            return false;
        }
        long count = inputs.stream().filter(
                i -> StringUtils.isNotEmpty(i.getProductId()) && i.getProductNum() != null && i.getProductNum() > ProductConstant.ZERO
        ).count();
        if (inputs.size() != count) {
            return ProductConstant.FALSE;
        }
        try {
            Boolean result = productService.deductionStock(inputs);
            if (result) {
                Set<String> ids = inputs.stream().map(StockDeductionInput::getProductId).collect(Collectors.toSet());
                productService.updateStockCache(ids);
            }
            return ProductConstant.TRUE;
        } catch (ProductException e) {
            log.error("[product]-batchDeductionStock err! param:{}, msg:{}",
                    inputs,
                    Throwables.getStackTraceAsString(e));
            return ProductConstant.FALSE;
        }
    }

    @Override
    public List<ProductOutput> productDetails(Set<String> productIds) {
        if (CollectionUtil.isEmpty(productIds)) {
            return new ArrayList<>();
        }
        return productService.productDetails(productIds);
    }

    @Override
    public Map<String, Long> productStock(Set<String> productIds) {
        return productService.productStock(productIds);
    }


}
