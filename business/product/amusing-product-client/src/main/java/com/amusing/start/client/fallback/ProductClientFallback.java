package com.amusing.start.client.fallback;

import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.input.ShopProductIdInput;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ProductOutput;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

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
            public boolean deductionStock(List<StockDeductionInput> inputs) {
                log.error("[product]-deductionStock fallback! param:{}", inputs);
                return false;
            }

            @Override
            public List<ProductOutput> getProductDetails(List<ShopProductIdInput> ids) {
                log.error("[product]-getProductDetails fallback! param:{}", ids);
                return null;
            }
        };
    }
}
