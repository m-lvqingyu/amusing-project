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
import com.amusing.start.result.ApiResult;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
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
    public ApiResult gaoDeGeoCode(String address) {
        // 获取数字签名
        Map<String, Object> param = new HashMap<>(INITIAL_CAPACITY);
        param.put(MapConstant.PARAM_ADDRESS_KEY, address);
        param.put(MapConstant.PARAM_KEY, mapGaoDeKey);
        String sign = GaoDeUtils.getSign(mapGaoDeSecretKey, param);


        // 组装请求地址与参数
        String requestParam = buildGeoCodeRequestParam(address, sign);


        // 发送请求
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(requestParam, String.class);
        } catch (Exception e) {
            log.error("[map]-[gaoDeGeoCode]-err! address:{}, result:{}", address, Throwables.getStackTraceAsString(e));
            return ApiResult.result(CommCode.FAIL);
        }


        // 响应结果检查
        try {
            checkoutResponseIsEmpty(MapConstant.TITLE_GAO_DE_GEOCODE, responseEntity);
        } catch (MapException e) {
            return ApiResult.result(e.getResultCode());
        }


        // 返回结果解析
        JSONObject jsonObject = JSONUtil.parseObj(responseEntity.getBody());
        List<GeoCodeVo> geoCodeVos = geoCodeResultProcessing(jsonObject);
        return ApiResult.ok(geoCodeVos);
    }

    /**
     * 拼接请求地址
     *
     * @param address 地址
     * @param sign    签名
     * @return 请求地址
     */
    private String buildGeoCodeRequestParam(String address, String sign) {
        StringBuilder sb = new StringBuilder();
        sb.append(MapConstant.GEO_CODE_URL)
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
                .append(sign);
        return sb.toString();
    }

    /**
     * 响应结果校验
     *
     * @param tile           标题
     * @param responseEntity 响应结果
     * @throws MapException
     */
    public <T> void checkoutResponseIsEmpty(String tile, ResponseEntity<T> responseEntity) throws MapException {
        if (responseEntity == null) {
            log.warn("[map]-[{}]-response is null!", tile);
            throw new MapException(MapCode.RESULT_NULL);
        }
        int statusCode = responseEntity.getStatusCodeValue();
        if (HttpStatus.OK.value() != statusCode) {
            log.warn("[map]-[{}]-response err! result:{}", tile, responseEntity);
            throw new MapException(MapCode.SERVER_REQUEST_ERR);
        }
        T t = responseEntity.getBody();
        if (t == null) {
            log.warn("[map]-[{}]-response body is null! result:{}", tile, responseEntity);
            throw new MapException(MapCode.SERVER_REQUEST_ERR);
        }
    }

    /**
     * 响应结果解析
     *
     * @param jsonObject 响应结果
     * @return
     */
    private List<GeoCodeVo> geoCodeResultProcessing(JSONObject jsonObject) {
        List<GeoCodeVo> geoCodeVoList = new ArrayList<>();
        JSONArray array = jsonObject.get(MapConstant.RESULT_GEOCODES_KEY, JSONArray.class);
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
