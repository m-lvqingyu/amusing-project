package com.amusing.start.order.controller.outward;

import com.amusing.start.code.CommCode;
import com.amusing.start.controller.BaseController;
import com.amusing.start.order.dto.OrderCreateDto;
import com.amusing.start.order.enums.OrderCode;
import com.amusing.start.order.exception.OrderException;
import com.amusing.start.order.from.OrderCreateFrom;
import com.amusing.start.order.service.IOrderCreateService;
import com.amusing.start.order.service.IOrderService;
import com.amusing.start.order.vo.OrderDetailVO;
import com.amusing.start.result.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Create By 2021/8/21
 *
 * @author lvqingyu
 */
@RestController
@RequestMapping("/order")
public class OrderOutwardController extends BaseController {

    @Autowired
    public OrderOutwardController(HttpServletRequest request, IOrderService orderService, IOrderCreateService orderCreateService) {
        super(request);
        this.orderService = orderService;
        this.orderCreateService = orderCreateService;
    }

    private final IOrderService orderService;

    private final IOrderCreateService orderCreateService;

    /**
     * 根据ID，获取订单详情
     *
     * @param id 订单ID
     * @return
     * @throws OrderException
     */
    @GetMapping("/{id}")
    public ApiResult get(@PathVariable("id") String id) throws OrderException {
        if (StringUtils.isEmpty(id)) {
            throw new OrderException(CommCode.PARAMETER_EXCEPTION);
        }

        String userId = getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new OrderException(CommCode.UNAUTHORIZED);
        }
        OrderDetailVO orderDetailVO = orderService.get(id, userId);
        return ApiResult.ok(orderDetailVO);
    }

    /**
     * 创建用户
     *
     * @param from
     * @return
     */
    @PostMapping
    public ApiResult create(@Valid @RequestBody OrderCreateFrom from) throws OrderException {
        String userId = getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new OrderException(CommCode.UNAUTHORIZED);
        }

        OrderCreateDto orderCreateDto = new OrderCreateDto();
        BeanUtils.copyProperties(from, orderCreateDto);
        String orderId = orderCreateService.create(orderCreateDto);
        if (StringUtils.isEmpty(orderId)) {
            return ApiResult.fail(OrderCode.ORDER_SAVE_FAIL);
        }
        return ApiResult.ok(orderId);
    }

}
