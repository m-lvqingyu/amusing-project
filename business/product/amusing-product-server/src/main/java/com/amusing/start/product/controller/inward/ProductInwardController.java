package com.amusing.start.product.controller.inward;

import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.product.constant.ProductConstant;
import com.amusing.start.product.service.IProductService;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiResult<Boolean> deductionStock(List<StockDeductionInput> inputs) {
        if (CollectionUtil.isEmpty(inputs)) {
            return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
        }
        long count = inputs.stream().filter(
                i -> StringUtils.isNotEmpty(i.getProductId()) && i.getProductNum() != null && i.getProductNum() > ProductConstant.ZERO).count();
        if (inputs.size() != count) {
            return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
        }
        Boolean result = productService.deductionStock(inputs);
        if (result) {
            Set<String> ids = inputs.stream().map(StockDeductionInput::getProductId).collect(Collectors.toSet());
            productService.updateStockCache(ids);
            return ApiResult.ok();
        }
        return ApiResult.result(CommCode.FREQUENT_OPERATION_EXCEPTION);
    }

    @Override
    public ApiResult<List<ProductOutput>> productDetails(Set<String> productIds) {
        if (CollectionUtil.isEmpty(productIds)) {
            return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
        }
        long count = productIds.stream().filter(StringUtils::isNotEmpty).count();
        if (count != productIds.size()) {
            return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
        }
        List<ProductOutput> details = productService.productDetails(productIds);
        return ApiResult.ok(details);
    }

    @Override
    public ApiResult<Map<String, Long>> productStock(Set<String> productIds) {
        if (CollectionUtil.isEmpty(productIds)) {
            return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
        }
        Map<String, Long> stocks = productService.productStock(productIds);
        if (CollectionUtil.isEmpty(stocks)) {
            return ApiResult.result(CommCode.RESULT_NOT_FOUND);
        }
        if (stocks.size() != productIds.size()) {
            return ApiResult.result(CommCode.RESULT_NOT_FOUND);
        }
        return ApiResult.ok(stocks);
    }


}
