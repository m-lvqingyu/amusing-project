package com.amusing.start.client.api;

import com.amusing.start.client.fallback.ShopSearchClientFallback;
import com.amusing.start.client.input.ShopPageInput;
import com.amusing.start.client.output.ShopOutput;
import com.amusing.start.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author lv.qingyu
 */
@FeignClient(value = "amusing-search-server", fallbackFactory = ShopSearchClientFallback.class)
public interface ShopSearchClient {

    /**
     * 分页获取商铺信息集合
     *
     * @param input 查询条件
     * @return 商铺信息集合
     */
    @PostMapping("/search/inward/shop/v1/page")
    ApiResult<List<ShopOutput>> shopPage(@RequestBody ShopPageInput input);

    /**
     * 获取商铺信息
     *
     * @param id 商铺ID
     * @return
     */
    @GetMapping("/search/inward/shop/v1/detail/{id}")
    ApiResult<ShopOutput> getDetail(@PathVariable("id") String id);
}
