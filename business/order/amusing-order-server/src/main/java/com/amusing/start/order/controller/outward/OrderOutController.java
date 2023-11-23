package com.amusing.start.order.controller.outward;

import com.amusing.start.annotation.RequestLimit;
import com.amusing.start.controller.BaseController;
import com.amusing.start.order.entity.dto.CreateDto;
import com.amusing.start.order.entity.vo.OrderDetailVo;
import com.amusing.start.order.service.OrderService;
import com.amusing.start.result.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Api(tags = "订单管理")
@RestController
@RequestMapping("/order/out")
public class OrderOutController extends BaseController {

    private final OrderService orderService;

    @Autowired
    public OrderOutController(HttpServletRequest request, OrderService orderService) {
        super(request);
        this.orderService = orderService;
    }

    @ApiOperation("创建订单")
    @RequestLimit(time = 3, count = 1)
    @PostMapping("create")
    public ApiResult<String> create(@Valid @RequestBody CreateDto dto) {
        return ApiResult.ok(orderService.create(getUserId(), dto));
    }

    @ApiOperation("订单详情")
    @ApiImplicitParam(name = "no", value = "订单编号", required = true)
    @GetMapping("detail/{no}")
    public ApiResult<OrderDetailVo> get(@NotBlank(message = "订单编号不能为空")
                                        @Pattern(regexp = "\\d{14,31}$", message = "订单编号格式错误")
                                        @PathVariable("no") String orderNo) {
        return ApiResult.ok(orderService.getOrderDetail(getUserId(), orderNo));
    }

}
