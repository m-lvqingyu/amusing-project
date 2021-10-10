package com.amusing.start.order.controller.outward;

import com.amusing.start.client.api.SettlementClient;
import com.amusing.start.order.controller.BaseController;
import com.amusing.start.order.exception.OrderException;
import com.amusing.start.order.service.IOrderService;
import com.amusing.start.order.vo.OrderDetailVO;
import com.amusing.start.result.ApiCode;
import com.amusing.start.result.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Create By 2021/8/21
 *
 * @author lvqingyu
 */
@RestController
@RequestMapping("/order")
public class OrderOutwardController extends BaseController {

    @Autowired
    public OrderOutwardController(HttpServletRequest request, IOrderService orderService, SettlementClient settlementClient) {
        super(request);
        this.orderService = orderService;
        this.settlementClient = settlementClient;
    }

    private final SettlementClient settlementClient;

    private final IOrderService orderService;

    @GetMapping("/{id}")
    public ApiResult get(@PathVariable("id") String id) throws OrderException {
        if (StringUtils.isEmpty(id)) {
            throw new OrderException(ApiCode.PARAMETER_EXCEPTION);
        }
        String userId = getUserId();
        OrderDetailVO orderDetailVO = orderService.get(id, userId);
        return ApiResult.ok(orderDetailVO);
    }
    
}
