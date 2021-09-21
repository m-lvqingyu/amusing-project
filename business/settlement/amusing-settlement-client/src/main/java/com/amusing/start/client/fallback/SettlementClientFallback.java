package com.amusing.start.client.fallback;

import com.amusing.start.client.api.SettlementClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * Create By 2021/9/12
 *
 * @author lvqingyu
 */
@Component
public class SettlementClientFallback implements FallbackFactory<SettlementClient> {

    @Override
    public SettlementClient create(Throwable throwable) {
        return new SettlementClient() {
            @Override
            public void settleAmount(String id) {
                System.out.println("降级了服务");
            }
        };
    }
}
