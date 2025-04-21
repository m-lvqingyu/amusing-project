package com.amusing.start.order.controller;

import com.amusing.start.client.api.OrderFeignClient;
import com.amusing.start.client.response.OrderDetailResponse;
import com.amusing.start.order.pojo.Order;
import com.amusing.start.order.enums.OrderErrorCode;
import com.amusing.start.order.service.OrderService;
import com.amusing.start.result.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/10/30
 */
@RestController
public class FeignOrderController implements OrderFeignClient {

    private final OrderService orderService;

    @Autowired
    public FeignOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ApiResult<OrderDetailResponse> detail(String orderNo) {
        Order orderInfo = orderService.getByNo(orderNo);
        if (orderInfo == null) {
            return ApiResult.result(OrderErrorCode.ORDER_NOT_FOUND);
        }
        return ApiResult.ok(new OrderDetailResponse().setOrderNo(orderNo).setRealAmount(orderInfo.getRealAmount()));
    }

}
