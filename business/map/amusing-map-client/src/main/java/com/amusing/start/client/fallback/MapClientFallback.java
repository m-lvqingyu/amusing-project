package com.amusing.start.client.fallback;

import com.amusing.start.client.api.MapClient;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Create By 2021/11/26
 *
 * @author lvqingyu
 */
@Slf4j
@Component
public class MapClientFallback implements FallbackFactory<MapClient> {

    @Override
    public MapClient create(Throwable throwable) {
        return new MapClient() {
            @Override
            public ApiResult gaoDeGeoCode(String address) {
                log.error("[map]-[gaoDeGeoCode]-service is degradation! address:{}", address);
                return ApiResult.result(CommCode.AUTHORITY_ERROR);
            }
        };
    }

}
