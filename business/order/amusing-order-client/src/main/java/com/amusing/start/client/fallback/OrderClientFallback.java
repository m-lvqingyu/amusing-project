package com.amusing.start.client.fallback;

import com.amusing.start.client.api.OrderClient;
import com.amusing.start.client.input.AliPayAsyncNotifyInput;
import com.amusing.start.client.output.OrderDetailOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 订单对内服务接口
 * @date 2021/11/5 22:58
 */
@Slf4j
@Component
public class OrderClientFallback implements FallbackFactory<OrderClient> {

    @Override
    public OrderClient create(Throwable throwable) {

        return new OrderClient() {
            @Override
            public ApiResult<OrderDetailOutput> orderDetail(String orderNo) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<Boolean> aliPayNotifySuccess(AliPayAsyncNotifyInput aliPayAsyncNotifyInput) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }
        };
    }

}
