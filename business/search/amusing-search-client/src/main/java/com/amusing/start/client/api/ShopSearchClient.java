package com.amusing.start.client.api;

import com.amusing.start.client.fallback.ShopSearchClientFallback;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lv.qingyu
 */
@FeignClient(value = "amusing-search-server", fallbackFactory = ShopSearchClientFallback.class)
public interface ShopSearchClient {

}
