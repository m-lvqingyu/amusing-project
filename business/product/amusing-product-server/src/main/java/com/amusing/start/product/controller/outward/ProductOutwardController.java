package com.amusing.start.product.controller.outward;

import com.amusing.start.controller.BaseController;
import com.amusing.start.exception.CustomException;
import com.amusing.start.product.entity.dto.ProductCreateDto;
import com.amusing.start.product.service.IProductService;
import com.amusing.start.result.ApiResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Administrator
 * @version 1.0
 * @date 2021/12/24
 */
@Api(tags = "商品")
@RestController
@RequestMapping("/product/outward")
public class ProductOutwardController extends BaseController {

    private final IProductService productService;

    @Autowired
    public ProductOutwardController(HttpServletRequest request, IProductService productService) {
        super(request);
        this.productService = productService;
    }

    @PostMapping("v1/create")
    public ApiResult<String> v1Create(@Valid @RequestBody ProductCreateDto dto) throws CustomException {
        return ApiResult.ok(productService.create(getUserId(), dto));
    }


}
