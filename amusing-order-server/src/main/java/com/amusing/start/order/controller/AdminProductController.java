package com.amusing.start.order.controller;

import com.amusing.start.controller.BaseController;
import com.amusing.start.order.biz.ProductBiz;
import com.amusing.start.order.req.AdminProductCreateReq;
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
@RequestMapping("/admin/product")
public class AdminProductController extends BaseController {

    private final ProductBiz productBiz;

    @Autowired
    public AdminProductController(HttpServletRequest request, ProductBiz productBiz) {
        super(request);
        this.productBiz = productBiz;
    }

    @PostMapping("create")
    public ApiResult<String> v1Create(@Valid @RequestBody AdminProductCreateReq dto) {
        productBiz.create(getUserId(), dto);
        return ApiResult.ok();
    }

}
