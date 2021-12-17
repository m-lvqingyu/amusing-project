package com.amusing.start.client.api;

import com.amusing.start.client.fallback.OrderClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Administrator
 * @version 1.0
 * @date 2021/11/5 22:58
 */
@FeignClient(name = "amusing-order-server", fallbackFactory = OrderClientFallback.class)
public interface OrderClient {

}
