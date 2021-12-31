package com.amusing.start.client.fallback;

import com.amusing.start.client.api.ShopSearchClient;
import com.amusing.start.client.input.ShopPageInput;
import com.amusing.start.client.output.ShopOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lv.qingyu
 */
@Slf4j
@Component
public class ShopSearchClientFallback implements FallbackFactory<ShopSearchClient> {
    @Override
    public ShopSearchClient create(Throwable throwable) {
        return new ShopSearchClient() {
            @Override
            public ApiResult<List<ShopOutput>> shopPage(ShopPageInput input) {
                log.error("[search]-shop page fallback! param:{}", input);
                return ApiResult.result(CommCode.DEGRADE_ERROR);
            }

            @Override
            public ApiResult<ShopOutput> getDetail(String id) {
                log.error("[search]-shop get detail fallback! param:{}", id);
                return ApiResult.result(CommCode.DEGRADE_ERROR);
            }
        };
    }
}
