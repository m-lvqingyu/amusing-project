package com.amusing.start.client.api;

import com.amusing.start.client.fallback.SettlementClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Create By 2021/9/12
 *
 * @author lvqingyu
 */
@FeignClient(name = "amusing-settlement-server", fallbackFactory = SettlementClientFallback.class)
public interface SettlementClient {

    @GetMapping("/settleAmount/{id}")
    void settleAmount(@PathVariable("id") String id);
}
