package com.amusing.start.client.fallback;

import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.client.output.ShopOutput;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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
            public boolean deductionStock(List<StockDeductionInput> inputs) {
                log.error("[product]-deductionStock fallback! param:{}", inputs);
                return false;
            }

            @Override
            public Map<String, ProductOutput> getProductDetails(Set<String> productIds) {
                log.error("[product]-getProductDetails fallback! param:{}", productIds);
                return new HashMap<>();
            }

            @Override
            public Map<String, ShopOutput> getShopDetails(Set<String> shopIds) {
                log.error("[product]-getShopDetails fallback! param:{}", shopIds);
                return new HashMap<>();
            }
        };
    }
}
