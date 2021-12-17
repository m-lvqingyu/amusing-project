package com.amusing.start.map.service;

import com.amusing.start.result.ApiResult;

/**
 * Create By 2021/11/26
 *
 * @author lvqingyu
 */
public interface IGaoDeGeoCodeService {

    /**
     * 高德- 地理编码
     *
     * @param address 地址
     * @return 地址--转化-->经纬度
     */
    ApiResult gaoDeGeoCode(String address);
}
