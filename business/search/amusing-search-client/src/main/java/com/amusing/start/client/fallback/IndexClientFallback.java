package com.amusing.start.client.fallback;

import com.amusing.start.client.api.IndexClient;
import com.amusing.start.client.input.IndexCreateInput;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lv.qingyu
 */
@Slf4j
@Component
public class IndexClientFallback implements FallbackFactory<IndexClient> {

    private static final boolean FAIL_FLAG = false;

    @Override
    public IndexClient create(Throwable throwable) {
        return new IndexClient() {

            @Override
            public ApiResult<Boolean> create(IndexCreateInput input) {
                log.error("[search]-create index fallback! param:{}", input);
                return ApiResult.result(CommCode.DEGRADE_ERROR, FAIL_FLAG);
            }

            @Override
            public ApiResult<Boolean> exist(String index) {
                log.error("[search]-check index exist fallback! param:{}", index);
                return ApiResult.result(CommCode.DEGRADE_ERROR, FAIL_FLAG);
            }
        };
    }
}
