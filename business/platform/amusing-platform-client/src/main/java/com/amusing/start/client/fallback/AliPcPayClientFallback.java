package com.amusing.start.client.fallback;

import com.amusing.start.client.api.AliPcPayClient;
import com.amusing.start.client.output.AliPayTradeOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Lv.QingYu
 * @description: 在FeignClient中，可以通过制定fallback，实现在服务不可用时自动调用fallback指定定的处理方法
 * @since 2023/10/17
 */
@Component
public class AliPcPayClientFallback implements FallbackFactory<AliPcPayClient> {
    @Override
    public AliPcPayClient create(Throwable throwable) {
        return new AliPcPayClient() {

            @Override
            public ApiResult<Boolean> close(String orderNo) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<Boolean> refund(String tradeNo, Integer amount, String refundNo) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<Boolean> refundQuery(String tradeNo, String refundNo) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<AliPayTradeOutput> query(String orderNo, String tradeNo) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<Boolean> asyncNotifyFinish(Long id) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<Boolean> asyncNotifyIgnore(Long id) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }
        };
    }
}
