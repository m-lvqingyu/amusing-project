package com.amusing.start.client.api;

import com.amusing.start.client.fallback.ProductClientFallback;
import com.amusing.start.client.output.ProductOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@FeignClient(name = "amusing-product-server", fallbackFactory = ProductClientFallback.class)
@RequestMapping("product")
public interface ProductClient {

    /**
     * 获取商品详情
     *
     * @param shopId    商铺ID
     * @param productId 商品ID
     * @param priceId   价格ID
     * @return
     */
    @GetMapping("/{shopId}/{productId}/{priceId}")
    ProductOutput get(@PathVariable(value = "shopId") String shopId,
                      @PathVariable(value = "productId") String productId,
                      @PathVariable(value = "priceId") String priceId);
}
