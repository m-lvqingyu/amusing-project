package com.amusing.start.map.controller.inward;

import com.amusing.start.client.api.MapClient;
import com.amusing.start.code.CommCode;
import com.amusing.start.map.service.IGaoDeGeoCodeService;
import com.amusing.start.result.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create By 2021/11/26
 *
 * @author lvqingyu
 */
@RestController
public class MapInwardController implements MapClient {

    private IGaoDeGeoCodeService gaoDeMapService;

    @Autowired
    public MapInwardController(IGaoDeGeoCodeService gaoDeMapService) {
        this.gaoDeMapService = gaoDeMapService;
    }

    @Override
    public ApiResult gaoDeGeoCode(String address) {
        if (StringUtils.isEmpty(address)) {
            return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
        }
        return gaoDeMapService.gaoDeGeoCode(address);
    }


}
