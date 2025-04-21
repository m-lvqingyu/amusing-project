package com.amusing.start.client.fallback;

import com.amusing.start.client.api.OrderFeignClient;
import com.amusing.start.client.response.OrderDetailResponse;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Lv.QingYu
 * @since 2021/11/5
 */
@Slf4j
@Component
public class OrderFeignClientFallback implements FallbackFactory<OrderFeignClient> {

    @Override
    public OrderFeignClient create(Throwable throwable) {

        return new OrderFeignClient() {
            @Override
            public ApiResult<OrderDetailResponse> detail(String orderNo) {
                return ApiResult.result(CommunalCode.DEGRADE_ERR);
            }
        };

    }

}
