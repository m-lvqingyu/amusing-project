package com.amusing.start.product.controller.outward;

import com.amusing.start.client.output.ShopCarOutput;
import com.amusing.start.controller.BaseController;
import com.amusing.start.product.business.ProductBusiness;
import com.amusing.start.product.entity.dto.ProductCreateDto;
import com.amusing.start.product.entity.dto.ShopCarCreateDto;
import com.amusing.start.product.entity.dto.ShopCreateDto;
import com.amusing.start.result.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 商铺/商品管理
 * @since 2021/12/24
 */
@Api(tags = "商铺/商品管理")
@RestController
@RequestMapping("/product/out")
public class ProductOutController extends BaseController {

    private final ProductBusiness productBusiness;

    @Autowired
    public ProductOutController(HttpServletRequest request, ProductBusiness productBusiness) {
        super(request);
        this.productBusiness = productBusiness;
    }

    @ApiOperation("商铺新增")
    @PostMapping("shop/create")
    public ApiResult<String> shopCreate(@Valid @RequestBody ShopCreateDto dto) {
        return ApiResult.ok(productBusiness.createShop(getUserId(), dto));
    }

    @ApiOperation("商品新增")
    @PostMapping("create")
    public ApiResult<String> v1Create(@Valid @RequestBody ProductCreateDto dto) {
        return ApiResult.ok(productBusiness.productCreate(getUserId(), dto));
    }

    @ApiOperation("购物车新增")
    @PostMapping("shopping")
    public ApiResult<Boolean> shopping(@Valid @RequestBody ShopCarCreateDto dto) {
        return ApiResult.ok(productBusiness.shopping(getUserId(), dto));
    }

    @ApiOperation("查询购物车")
    @GetMapping("car")
    public ApiResult<List<ShopCarOutput>> getShopCar() {
        return ApiResult.ok(productBusiness.shopCar(getUserId()));
    }
}
