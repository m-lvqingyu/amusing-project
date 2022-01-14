package com.amusing.start.map.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.amusing.start.code.CommCode;
import com.amusing.start.map.constant.MapConstant;
import com.amusing.start.map.enums.GaoDeGeoCodeLevel;
import com.amusing.start.map.enums.MapCode;
import com.amusing.start.map.exception.MapException;
import com.amusing.start.map.service.IGaoDeGeoCodeService;
import com.amusing.start.map.utils.GaoDeUtils;
import com.amusing.start.map.vo.GeoCodeVo;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Create By 2021/11/26
 *
 * @author lvqingyu
 */
@Slf4j
@Service
public class GaoDeGeoCodeServiceImpl implements IGaoDeGeoCodeService {

    private final RestTemplate restTemplate;

    @Autowired
    public GaoDeGeoCodeServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${map.gao.de.key}")
    private String mapGaoDeKey;
    @Value("${map.gao.de.secret.key}")
    private String mapGaoDeSecretKey;
    private static final int INITIAL_CAPACITY = 2;

    @Override
    public List<GeoCodeVo> gaoDeGeoCode(String address) throws MapException {
        // 获取数字签名
        String sign = getGeoCodeRequestSign(address);
        // 组装请求地址与参数
        String requestParam = buildGeoCodeRequestParam(address, sign);
        // 发送请求
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(requestParam, String.class);
        } catch (Exception e) {
            log.error("[map]-gaoDeGeoCode err! address:{}, msg:{}", address, Throwables.getStackTraceAsString(e));
            throw new MapException(CommCode.FREQUENT_OPERATION_EXCEPTION);
        }
        Optional.of(responseEntity).map(ResponseEntity::getStatusCodeValue).filter(code -> HttpStatus.OK.value() == code)
                .orElseThrow(() -> new MapException(MapCode.SERVER_REQUEST_ERR));
        // 返回结果解析
        return geoCodeResultProcessing(responseEntity.getBody());
    }

    /**
     * 获取验签
     *
     * @param address 地址
     * @return 验签
     */
    private String getGeoCodeRequestSign(String address) {
        Map<String, Object> param = new HashMap<>(INITIAL_CAPACITY);
        param.put(MapConstant.PARAM_ADDRESS_KEY, address);
        param.put(MapConstant.PARAM_KEY, mapGaoDeKey);
        return GaoDeUtils.getSign(mapGaoDeSecretKey, param);
    }

    /**
     * 拼接请求地址
     *
     * @param address 地址
     * @param sign    签名
     * @return 请求地址
     */
    private String buildGeoCodeRequestParam(String address, String sign) {
        return new StringBuilder()
                .append(MapConstant.GEO_CODE_URL)
                .append(MapConstant.PARAM_KEY)
                .append(MapConstant.EQUAL_SIGN)
                .append(mapGaoDeKey)
                .append(MapConstant.AND_SIGN)
                .append(MapConstant.PARAM_ADDRESS_KEY)
                .append(MapConstant.EQUAL_SIGN)
                .append(address)
                .append(MapConstant.AND_SIGN)
                .append(MapConstant.PARAM_SIG)
                .append(MapConstant.EQUAL_SIGN)
                .append(sign)
                .toString();
    }

    /**
     * 响应结果解析
     *
     * @param geoCodeStr 响应结果
     * @return
     */
    private List<GeoCodeVo> geoCodeResultProcessing(String geoCodeStr) throws MapException {
        List<GeoCodeVo> geoCodeVoList = new ArrayList<>();
        if (StringUtils.isEmpty(geoCodeStr)) {
            return geoCodeVoList;
        }
        JSONArray array;
        try {
            array = JSONUtil.parseObj(geoCodeStr).get(MapConstant.RESULT_GEOCODES_KEY, JSONArray.class);
        } catch (Exception e) {
            log.error("[map]-gaoDeGeoCode resultProcessing err! geoCodeStr:{}, msg:{}", geoCodeStr, Throwables.getStackTraceAsString(e));
            throw new MapException(MapCode.RESULT_PROCESSING_ERR);
        }
        Iterator<Object> iterator = array.stream().iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            JSONObject geoCode = JSONUtil.parseObj(next);
            String location = geoCode.get(MapConstant.RESULT_GEOCODES_LOCATION_KEY, String.class);
            String level = geoCode.get(MapConstant.RESULT_GEOCODES_LEVEL_KEY, String.class);
            GaoDeGeoCodeLevel codeLevel = GaoDeGeoCodeLevel.gaoDeGeoCodeLevel(level);
            GeoCodeVo geoCodeVo = GeoCodeVo.builder().location(location).level(codeLevel.getKey()).build();
            geoCodeVoList.add(geoCodeVo);
        }
        return geoCodeVoList;
    }
}
