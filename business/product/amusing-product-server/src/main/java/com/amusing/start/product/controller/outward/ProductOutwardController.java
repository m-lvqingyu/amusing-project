package com.amusing.start.product.controller.outward;

import com.amusing.start.controller.BaseController;
import com.amusing.start.exception.UnauthorizedException;
import com.amusing.start.product.dto.create.ProductCreateDto;
import com.amusing.start.product.enums.ProductCode;
import com.amusing.start.product.exception.ProductException;
import com.amusing.start.product.from.create.ProductCreateFrom;
import com.amusing.start.product.service.IProductService;
import com.amusing.start.result.ApiResult;
import org.apache.commons.lang3.StringUtils;
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
@RestController
@RequestMapping("outward")
public class ProductOutwardController extends BaseController {

    @Autowired
    private final IProductService productService;

    public ProductOutwardController(HttpServletRequest request, IProductService productService) {
        super(request);
        this.productService = productService;
    }

    /**
     * 创建商品
     *
     * @param createFrom 商品信息
     * @return
     */
    @PostMapping("v1/create")
    public ApiResult<String> v1Create(@Valid @RequestBody ProductCreateFrom createFrom) throws ProductException, UnauthorizedException {
        ProductCreateDto createDto = fromToDto(createFrom);
        String productId = productService.create(getUserId(), createDto);
        return StringUtils.isNotEmpty(productId) ? ApiResult.ok(productId) : ApiResult.result(ProductCode.PRODUCT_CREATE_ERR);
    }

    private ProductCreateDto fromToDto(ProductCreateFrom createFrom) {
        return ProductCreateDto.builder()
                .shopId(createFrom.getShopId())
                .productName(createFrom.getName())
                .productStock(createFrom.getStock())
                .productPrice(createFrom.getPrice())
                .describe(createFrom.getDescribe())
                .build();
    }


}
