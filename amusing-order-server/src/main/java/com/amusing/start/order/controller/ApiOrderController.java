package com.amusing.start.order.controller;

import com.amusing.start.controller.BaseController;
import com.amusing.start.log.annotation.OperationLog;
import com.amusing.start.log.enums.OperateType;
import com.amusing.start.order.biz.OrderBiz;
import com.amusing.start.order.req.ApiCreateOrderReq;
import com.amusing.start.result.ApiResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Lv.QingYu
 * @since 2024/3/11
 */
@Validated
@RestController
@RequestMapping("/api/order")
public class ApiOrderController extends BaseController {

    private final OrderBiz orderBiz;

    public ApiOrderController(HttpServletRequest request, OrderBiz orderBiz) {
        super(request);
        this.orderBiz = orderBiz;
    }

    @OperationLog(type = OperateType.ADD, value = "创建订单",
            expression = "T(com.alibaba.fastjson.JSON).toJSONString(#req)")
    @PostMapping("create")
    public ApiResult<String> create(@Valid @RequestBody ApiCreateOrderReq req) {
        return ApiResult.ok(orderBiz.create(getUserId(), req));
    }

}