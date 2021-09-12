package com.amusing.start.order.controller.outward;

import com.amusing.start.client.api.SettlementClient;
import com.amusing.start.order.controller.BaseController;
import com.amusing.start.order.exception.OrderException;
import com.amusing.start.result.ApiResult;
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

    public OrderOutwardController(HttpServletRequest request) {
        super(request);
    }

    @Autowired
    private SettlementClient settlementClient;

    @GetMapping("admin/get/{id}")
    public ApiResult get(@PathVariable("id") String id) throws OrderException, InterruptedException {
        System.out.println(System.currentTimeMillis());
        Thread.sleep(200);
        settlementClient.settleAmount(id);
        String userId = getUserId();
        System.out.println("我只想了");
        return ApiResult.ok();
    }
}
