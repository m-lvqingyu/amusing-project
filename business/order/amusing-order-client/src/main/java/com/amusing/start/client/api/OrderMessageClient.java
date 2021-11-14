package com.amusing.start.client.api;

import com.amusing.start.client.fallback.OrderClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * Create By 2021/11/14
 *
 * @author lvqingyu
 */
@FeignClient(name = "amusing-order-server", fallbackFactory = OrderClientFallback.class)
public interface OrderMessageClient {

    @PutMapping("/inward/order/msg/product/success/v1/{txId}")
    void productSuccess(@PathVariable("txId") String txId);

    @PutMapping("/inward/order/msg/product/fail/v1/{txId}")
    void productFail(@PathVariable("txId") String txId);

    @PutMapping("/inward/order/msg/user/success/v1/{txId}")
    void userSuccess(@PathVariable("txId") String txId);

    @PutMapping("/inward/order/msg/user/fail/v1/{txId}")
    void userFail(@PathVariable("txId") String txId);
}
