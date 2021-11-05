package com.amusing.start.order.controller.inward;

import com.amusing.start.client.api.OrderClient;
import com.amusing.start.order.service.IOrderService;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2021/11/5 22:25
 */
@RestController
public class OrderInwardController implements OrderClient {

    private final IOrderService orderService;

    public OrderInwardController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public Boolean isCancel(String orderNo) {
        return orderService.isCancel(orderNo);
    }

}
