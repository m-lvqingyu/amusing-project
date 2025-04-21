package com.amusing.start.client.api;

import com.amusing.start.client.fallback.OrderFeignClientFallback;
import com.amusing.start.client.response.OrderDetailResponse;
import com.amusing.start.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lv.QingYu
 * @since 2021/11/5
 */
@FeignClient(name = "amusing-order-server",
        fallbackFactory = OrderFeignClientFallback.class,
        contextId = "order-client")
public interface OrderFeignClient {

    @GetMapping("inward/order/info/detail")
    ApiResult<OrderDetailResponse> detail(@RequestParam("orderNo") String orderNo);

}
