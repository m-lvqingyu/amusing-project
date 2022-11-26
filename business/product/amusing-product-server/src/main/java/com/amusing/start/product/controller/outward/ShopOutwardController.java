package com.amusing.start.product.controller.outward;

import com.amusing.start.controller.BaseController;
import com.amusing.start.exception.CustomException;
import com.amusing.start.product.entity.dto.ShopCreateDto;
import com.amusing.start.product.service.IShopService;
import com.amusing.start.result.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author lv.qingyu
 * @version 1.0
 * @date 2021/12/24
 */
@RestController
@RequestMapping("/product/outward")
public class ShopOutwardController extends BaseController {

    private final IShopService shopService;

    @Autowired
    public ShopOutwardController(HttpServletRequest request, IShopService shopService) {
        super(request);
        this.shopService = shopService;
    }

    @PostMapping("v1/shop/create")
    public ApiResult<String> v1Create(@Valid @RequestBody ShopCreateDto dto) throws CustomException {
        return ApiResult.ok(shopService.create(getUserId(), dto));
    }

}
