package com.amusing.start.client.api;

import com.amusing.start.client.fallback.OrderClientFallback;
import com.amusing.start.client.input.AliPayAsyncNotifyInput;
import com.amusing.start.client.output.OrderDetailOutput;
import com.amusing.start.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Administrator
 * @version 1.0
 * @date 2021/11/5 22:58
 */
@FeignClient(name = "amusing-order-server", fallbackFactory = OrderClientFallback.class)
public interface OrderClient {

    @GetMapping("order/in/info/detail")
    ApiResult<OrderDetailOutput> orderDetail(@RequestParam("orderNo") String orderNo);

    @PostMapping("order/in/ali/pay/notify/success")
    ApiResult<Boolean> aliPayNotifySuccess(@RequestBody AliPayAsyncNotifyInput aliPayAsyncNotifyInput);

}
