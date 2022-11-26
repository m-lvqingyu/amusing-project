package com.amusing.start.client.fallback;

import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ShopCarOutput;
import com.amusing.start.code.ErrorCode;
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
            public ApiResult<List<ShopCarOutput>> shopCar(String userId) {
                return ApiResult.result(ErrorCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<Boolean> deductionStock(List<StockDeductionInput> inputs) {
                return ApiResult.result(ErrorCode.DEGRADE_ERR);
            }
        };
    }
}
