package com.amusing.start.map.controller.inward;

import com.amusing.start.client.api.MapClient;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create By 2021/11/26
 *
 * @author lvqingyu
 */
@RestController
public class MapInwardController implements MapClient {


    @Override
    public String gaoDeGeoCode(String address) {
        return null;
    }


}
