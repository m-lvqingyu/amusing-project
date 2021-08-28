package com.amusing.start.order.controller.outward;

import com.amusing.start.order.controller.BaseController;
import com.amusing.start.order.exception.OrderException;
import com.amusing.start.result.ApiResult;
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
public class OrderInwardController extends BaseController {

    public OrderInwardController(HttpServletRequest request) {
        super(request);
    }

    @GetMapping("admin/get/{id}")
    public ApiResult get(@PathVariable("id") String id) throws OrderException {
        String userId = getUserId();
        return ApiResult.ok();
    }
}
