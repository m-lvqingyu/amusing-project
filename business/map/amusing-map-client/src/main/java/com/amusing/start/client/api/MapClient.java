package com.amusing.start.client.api;

import com.amusing.start.client.fallback.MapClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Create By 2021/11/26
 *
 * @author lvqingyu
 */
@FeignClient(name = "amusing-map-server", fallbackFactory = MapClientFallback.class)
public interface MapClient {

    /**
     * 高德-地理编码
     *
     * @param address 地址
     * @return 地址--转化-->经纬度
     */
    @GetMapping("/inward/map/v1/gd/geocode/{address}")
    String gaoDeGeoCode(@PathVariable("address") String address);


}
