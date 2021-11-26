package com.amusing.start.map.service;

/**
 * Create By 2021/11/26
 *
 * @author lvqingyu
 */
public interface IGaoDeMapService {

    /**
     * 高德- 地理编码
     *
     * @param address 地址
     * @return 地址--转化-->经纬度
     */
    String gaoDeGeoCode(String address);
}
