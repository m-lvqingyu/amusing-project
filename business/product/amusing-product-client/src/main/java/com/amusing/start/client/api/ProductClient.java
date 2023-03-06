package com.amusing.start.client.api;

import com.amusing.start.client.fallback.ProductClientFallback;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ShopCarOutput;
import com.amusing.start.exception.CustomException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@FeignClient(name = "amusing-product-server", fallbackFactory = ProductClientFallback.class)
public interface ProductClient {

    /**
     * 获取购物车中-商品信息详情
     *
     * @param userId 用户ID
     * @return 商品详情集合
     */
    @GetMapping("product/inward/v1/shop/car/{userId}")
    List<ShopCarOutput> shopCar(@PathVariable("userId") String userId) throws CustomException;


    /**
     * 扣减商品库存
     *
     * @param inputs 扣减库存信息
     * @return true: 成功 false: 失败
     */
    @PostMapping("product/inward/v1/stock/deduction")
    Boolean deductionStock(@RequestBody List<StockDeductionInput> inputs) throws CustomException;


}
