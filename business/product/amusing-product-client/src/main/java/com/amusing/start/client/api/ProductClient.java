package com.amusing.start.client.api;

import com.amusing.start.client.fallback.ProductClientFallback;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ShopCarOutput;
import com.amusing.start.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 商品库存服务内部接口
 * @since 2021/10/23
 */
@FeignClient(name = "amusing-product-server", fallbackFactory = ProductClientFallback.class)
public interface ProductClient {

    /**
     * @param userId 用户ID
     * @return 商品详情集合
     * @description: 获取购物车中-商品信息详情
     */
    @GetMapping("product/in/v1/shop/car/{userId}")
    List<ShopCarOutput> shopCar(@PathVariable("userId") String userId);


    /**
     * @param inputs 扣减库存信息
     * @return true: 成功 false: 失败
     * @description: 扣减商品库存
     */
    @PostMapping("product/in/v1/stock/deduction")
    ApiResult<Boolean> deductionStock(@RequestBody List<StockDeductionInput> inputs);


}
