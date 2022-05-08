package com.amusing.start.map.controller.outward;

import com.amusing.start.code.CommCode;
import com.amusing.start.controller.BaseController;
import com.amusing.start.map.exception.MapException;
import com.amusing.start.map.service.IGaoDeGeoCodeService;
import com.amusing.start.map.vo.GeoCodeVo;
import com.amusing.start.result.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lv.qingyu
 */
@RestController
@RequestMapping("/map/outward")
public class MapOutwardController extends BaseController {

    private final IGaoDeGeoCodeService gaoDeMapService;

    @Autowired
    public MapOutwardController(HttpServletRequest request, IGaoDeGeoCodeService gaoDeMapService) {
        super(request);
        this.gaoDeMapService = gaoDeMapService;
    }

    /**
     * 高德地图-地理编码
     *
     * @param address 地址
     * @return 坐标信息
     */
    @GetMapping("v1/gaoDeGeoCode/{address}")
    public ApiResult<List<GeoCodeVo>> gaoDeGeoCode(@PathVariable("address") String address) {
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
