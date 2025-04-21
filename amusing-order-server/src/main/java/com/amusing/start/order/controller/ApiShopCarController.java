package com.amusing.start.order.controller;

import com.amusing.start.controller.BaseController;
import com.amusing.start.order.biz.ShopCarBiz;
import com.amusing.start.order.req.ApiShoppingReq;
import com.amusing.start.order.resp.ApiShopCarDetailResp;
import com.amusing.start.result.ApiResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2025/3/6
 */
@Validated
@RestController
@RequestMapping("/api/order")
public class ApiShopCarController extends BaseController {

    private final ShopCarBiz shopCarBiz;

    public ApiShopCarController(HttpServletRequest request, ShopCarBiz shopCarBiz) {
        super(request);
        this.shopCarBiz = shopCarBiz;
    }

    @PostMapping("shopping")
    private ApiResult<Boolean> shopping(@RequestBody ApiShoppingReq req) {
        return ApiResult.ok(shopCarBiz.shopping(getUserId(), req));
    }

    @GetMapping("shop/car")
    public ApiResult<List<ApiShopCarDetailResp>> shopCar() {
        return ApiResult.ok(shopCarBiz.shopCar(getUserId()));
    }

}
