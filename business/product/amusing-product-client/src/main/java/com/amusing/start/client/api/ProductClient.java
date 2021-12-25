package com.amusing.start.client.api;

import com.amusing.start.client.fallback.ProductClientFallback;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.client.output.ShopOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    @PostMapping("/product/inward/v1/stock/deduction")
    boolean deductionStock(@RequestBody List<StockDeductionInput> inputs);

    /**
     * 获取商品详情
     *
     * @param productIds 商品ID集合
     * @return
     */
    @PostMapping("/product/inward/v1/details/{ids}")
    Map<String, ProductOutput> getProductDetails(@PathVariable("ids") Set<String> productIds);

    /**
     * 获取商铺详情
     *
     * @param shopIds 商铺id集合
     * @return
     */
    @PostMapping("/shop/inward/v1/details/{ids}")
    Map<String, ShopOutput> getShopDetails(@PathVariable("ids") Set<String> shopIds);

}
