package com.amusing.start.order.controller;

import com.amusing.start.controller.BaseController;
import com.amusing.start.order.req.AdminShopCreateReq;
import com.amusing.start.order.service.ShopService;
import com.amusing.start.result.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Lv.QingYu
 * @since 2021/12/24
 */
@RestController
@RequestMapping("/admin/shop")
public class AdminShopController extends BaseController {

    private final ShopService shopService;

    @Autowired
    public AdminShopController(HttpServletRequest request, ShopService shopService) {
        super(request);
        this.shopService = shopService;
    }

    @PostMapping("create")
    public ApiResult<Boolean> shopCreate(@Valid @RequestBody AdminShopCreateReq req) {
        shopService.create(getUserId(), req);
        return ApiResult.ok();
    }




}
