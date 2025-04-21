package com.amusing.start.order.controller;

import com.amusing.start.controller.BaseController;
import com.amusing.start.log.annotation.OperationLog;
import com.amusing.start.log.enums.OperateType;
import com.amusing.start.order.req.AdminOrderPageReq;
import com.amusing.start.order.resp.AdminOrderPageResp;
import com.amusing.start.order.resp.OrderDetailResp;
import com.amusing.start.order.permission.DataScope;
import com.amusing.start.order.service.OrderService;
import com.amusing.start.result.ApiResult;
import com.amusing.start.result.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Lv.QingYu
 * @since 2024/3/11
 */
@Valid
@RestController
@RequestMapping("/admin/order")
public class AdminOrderController extends BaseController {

    private final OrderService orderService;

    public AdminOrderController(HttpServletRequest request,
                                OrderService orderService) {
        super(request);
        this.orderService = orderService;
    }

    @OperationLog(
            type = OperateType.SELECT,
            value = "查询订单详情",
            expression = "T(com.alibaba.fastjson.JSON).toJSONString(#req)")
    @DataScope
    @GetMapping("page")
    public ApiResult<PageResult<AdminOrderPageResp>> page(AdminOrderPageReq req) {
        PageResult<AdminOrderPageResp> page = orderService.page(req);
        return ApiResult.ok(page);
    }

    @OperationLog(
            type = OperateType.SELECT,
            value = "查询订单详情",
            expression = "'orderNo:'+#orderNo")
    @DataScope
    @GetMapping("detail/{no}")
    public ApiResult<OrderDetailResp> get(@NotBlank(message = "订单编号不能为空")
                                          @Pattern(regexp = "\\d{14,31}$", message = "订单编号格式错误")
                                          @PathVariable("no") String orderNo) {
        return ApiResult.ok(orderService.getOrderDetail(orderNo));
    }

}
