package com.amusing.start.client.fallback;

import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.output.ProductOutput;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
            public ProductOutput get(String shopId, String productId, String priceId) {
                log.error("[Product]-[getProductDetail]-server degradation!shopId:{}, productId:{}, priceId:{}", shopId, priceId, priceId);
                return null;
            }
        };
    }
}
