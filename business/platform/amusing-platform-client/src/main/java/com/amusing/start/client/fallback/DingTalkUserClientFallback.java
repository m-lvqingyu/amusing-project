package com.amusing.start.client.fallback;

import com.amusing.start.client.api.DingTalkUserClient;
import com.amusing.start.client.output.DingTalkAccessToken;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Lv.QingYu
 * @since 2023/10/3
 */
@Slf4j
@Component
public class DingTalkUserClientFallback implements FallbackFactory<DingTalkUserClient> {

    @Override
    public DingTalkUserClient create(Throwable throwable) {
        return new DingTalkUserClient() {
            @Override
            public ApiResult<DingTalkAccessToken> getAccessToken() {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<String> getUserId(String phone) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }
        };
    }
}
