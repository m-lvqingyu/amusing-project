package com.amusing.start.order.controller.outward;

import com.amusing.start.controller.BaseController;
import com.amusing.start.exception.CustomException;
import com.amusing.start.order.entity.dto.CreateDto;
import com.amusing.start.order.entity.vo.OrderDetailVo;
import com.amusing.start.order.service.IOrderService;
import com.amusing.start.result.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Create By 2021/8/21
 *
 * @author lvqingyu
 */
@Slf4j
@RestController
@RequestMapping("/order/outward")
@Api(tags = "订单")
public class OrderInfoController extends BaseController {

    private final IOrderService orderService;

    @Autowired
    public OrderInfoController(HttpServletRequest request, IOrderService orderService) {
        super(request);
        this.orderService = orderService;
    }

    @ApiOperation("创建订单")
    @PostMapping("v1/create")
    public ApiResult<String> create(@Valid @RequestBody CreateDto dto) throws CustomException {
        return ApiResult.ok(orderService.create(getUserId(), dto));
    }

    @ApiOperation("订单详情")
    @ApiImplicitParam(name = "no", value = "订单编号", required = true)
    @GetMapping("v1/{no}")
    public ApiResult<OrderDetailVo> get(@PathVariable("no") String orderNo) throws CustomException {
        return ApiResult.ok(orderService.getOrderDetail(getUserId(), orderNo));
    }

}
