package com.amusing.start.map.service;

import com.amusing.start.map.exception.MapException;
import com.amusing.start.map.vo.GeoCodeVo;

import java.util.List;

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
     * @throws MapException
     */
    List<GeoCodeVo> gaoDeGeoCode(String address) throws MapException;
}
