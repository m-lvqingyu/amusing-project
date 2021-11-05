package com.amusing.start.client.fallback;

import com.amusing.start.client.api.OrderClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 订单对内服务接口
 * @date 2021/11/5 22:58
 */
@Slf4j
@Component
public class OrderClientFallback implements FallbackFactory<OrderClient> {

    @Override
    public OrderClient create(Throwable throwable) {

        return new OrderClient() {
            @Override
            public Boolean isCancel(String orderNo) {
                log.error("[Order]-[isCancel]-server degradation!orderNo:{}", orderNo);
                return null;
            }
        };
        
    }

}
