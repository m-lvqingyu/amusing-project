package com.amusing.start.client.api;

import com.amusing.start.client.fallback.MapClientFallback;
import com.amusing.start.result.ApiResult;
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
     * @param address 地址 规则遵循：国家、省份、城市、区县、城镇、乡村、街道、门牌号码、屋邨、大厦，如：北京市朝阳区阜通东大街6号
     * @return 地址--转化-->经纬度
     */
    @GetMapping("/inward/map/v1/gd/geocode/{address}")
    ApiResult gaoDeGeoCode(@PathVariable("address") String address);


}
