package com.amusing.start.product.controller.inward;

import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ShopOutput;
import com.amusing.start.product.exception.ProductException;
import com.amusing.start.product.service.IProductService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

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
    public boolean deductionStock(List<StockDeductionInput> inputs) {
        if (CollectionUtil.isEmpty(inputs)) {
            return false;
        }

        long count = inputs.stream()
                .filter(i -> StringUtils.isNotEmpty(i.getProductId()) && i.getProductNum() != null && i.getProductNum() > 0)
                .count();
        if (inputs.size() != count) {
            return false;
        }

        try {
            return productService.deductionStock(inputs);
        } catch (ProductException e) {
            log.error("[product]-deductionStock err! param:{}, msg:{}", inputs, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @Override
    public List<ShopOutput> getDetails(Set<String> productIds) {
        return productService.getDetails(productIds);
    }

}
