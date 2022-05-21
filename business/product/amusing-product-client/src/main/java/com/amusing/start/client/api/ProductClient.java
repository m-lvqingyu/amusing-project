package com.amusing.start.client.api;

import com.amusing.start.client.fallback.ProductClientFallback;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@FeignClient(name = "amusing-product-server", fallbackFactory = ProductClientFallback.class)
public interface ProductClient {

    /**
     * 扣减商品库存
     *
     * @param inputs 扣减库存信息
     * @return true: 成功 false: 失败
     */
    @PostMapping("product/inward/v1/stock/deduction")
    ApiResult<Boolean> deductionStock(@RequestBody List<StockDeductionInput> inputs);

    /**
     * 获取商品信息详情
     *
     * @param productIds 商品ID集合
     * @return 商品详情集合
     */
    @GetMapping("product/inward/v1/details")
    ApiResult<List<ProductOutput>> productDetails(@RequestParam("productIds") Set<String> productIds);

    /**
     * 获取商品库存信息
     *
     * @param productIds 商品ID集合
     * @return 商品库存信息
     */
    @GetMapping("product/inward/v1/stock")
    ApiResult<Map<String, Long>> productStock(@RequestParam("productIds") Set<String> productIds);

}
