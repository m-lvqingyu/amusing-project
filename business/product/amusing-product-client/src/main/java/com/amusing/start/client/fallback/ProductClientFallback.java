package com.amusing.start.client.fallback;

import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.input.ShopProductIdInput;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@Slf4j
@Component
public class ProductClientFallback implements FallbackFactory<ProductClient> {
    @Override
    public ProductClient create(Throwable throwable) {
        return new ProductClient() {

            @Override
            public ApiResult<Boolean> deductionStock(List<StockDeductionInput> inputs) {
                log.error("[Product]-deductionStock fallback! param:{}", inputs);
                return ApiResult.result(CommCode.DEGRADE_ERROR);
            }

            @Override
            public ApiResult<List<ProductOutput>> productDetails(Set<String> productIds) {
                log.error("[Product]-productDetails fallback! param:{}", productIds);
                return ApiResult.result(CommCode.DEGRADE_ERROR);
            }

            @Override
            public ApiResult<Map<String, Long>> productStock(Set<String> productIds) {
                log.error("[Product]-productStock fallback! param:{}", productIds);
                return ApiResult.result(CommCode.DEGRADE_ERROR);
            }
        };
    }
}
