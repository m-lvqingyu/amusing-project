package com.amusing.start.map.controller.inward;

import com.amusing.start.client.api.MapClient;
import com.amusing.start.code.CommCode;
import com.amusing.start.map.exception.MapException;
import com.amusing.start.map.service.IGaoDeGeoCodeService;
import com.amusing.start.map.vo.GeoCodeVo;
import com.amusing.start.result.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Create By 2021/11/26
 *
 * @author lvqingyu
 */
@RestController
public class MapInwardController implements MapClient {

    private final IGaoDeGeoCodeService gaoDeMapService;

    @Autowired
    public MapInwardController(IGaoDeGeoCodeService gaoDeMapService) {
        this.gaoDeMapService = gaoDeMapService;
    }

    @Override
    public ApiResult<List<GeoCodeVo>> gaoDeGeoCode(String address) {
        if (StringUtils.isEmpty(address)) {
            return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
        }
        try {
            List<GeoCodeVo> geoCodeVos = gaoDeMapService.gaoDeGeoCode(address);
            return ApiResult.ok(geoCodeVos);
        } catch (MapException e) {
            return ApiResult.result(e.getResultCode());
        }
    }

}
