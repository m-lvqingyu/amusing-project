package com.amusing.start.client.api;

import com.amusing.start.client.fallback.ProductClientFallback;
import com.amusing.start.client.input.ShopProductIdInput;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ProductOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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
     * 获取商品详情集合
     *
     * @param ids id集合
     * @return
     */
    @PostMapping("/product/inward/v1/details")
    List<ProductOutput> getProductDetails(@RequestBody List<ShopProductIdInput> ids);


}
