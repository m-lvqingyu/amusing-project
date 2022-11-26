package com.amusing.start.client.fallback;

import com.amusing.start.client.api.ShopSearchClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lv.qingyu
 */
@Slf4j
@Component
public class ShopSearchClientFallback implements FallbackFactory<ShopSearchClient> {
    @Override
    public ShopSearchClient create(Throwable throwable) {
        return new ShopSearchClient() {

        };
    }
}
