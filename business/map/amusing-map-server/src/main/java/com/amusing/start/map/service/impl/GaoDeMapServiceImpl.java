package com.amusing.start.map.service.impl;

import com.amusing.start.map.constant.MapConstant;
import com.amusing.start.map.service.IGaoDeMapService;
import com.amusing.start.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.Map;

/**
 * Create By 2021/11/26
 *
 * @author lvqingyu
 */
@Service
public class GaoDeMapServiceImpl implements IGaoDeMapService {

    private final RestTemplate restTemplate;

    @Autowired
    public GaoDeMapServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    // key 94bf7086cd152ef8ad750239492228cf
    // 94bf7086cd152ef8ad750239492228cf

    @Override
    public String gaoDeGeoCode(String address) {
        return null;
    }


    private String getSign(String secretKey, Map<String, Object> param) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> resultMap = MapUtils.sortByKey(param, false);
        Iterator<Map.Entry<String, Object>> iterator = resultMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            Object value = next.getValue();
            sb.append(MapConstant.AND_SIGN);
            sb.append(key);
            sb.append(MapConstant.EQUAL_SIGN);
            sb.append(value);
        }
        return sb.append(secretKey).toString().replaceFirst(MapConstant.AND_SIGN, "");
    }

}
