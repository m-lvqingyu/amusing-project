package com.amusing.start.product.controller.outward;

import com.amusing.start.code.CommCode;
import com.amusing.start.controller.BaseController;
import com.amusing.start.exception.UnauthorizedException;
import com.amusing.start.product.dto.create.ShopCreateDto;
import com.amusing.start.product.enums.ProductCode;
import com.amusing.start.product.exception.ProductException;
import com.amusing.start.product.from.create.ShopCreateFrom;
import com.amusing.start.product.service.IShopService;
import com.amusing.start.result.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

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

    /**
     * 开店
     *
     * @param from 店铺信息
     * @return
     * @throws ProductException
     */
    @PostMapping("v1/shop/create")
    public ApiResult<String> v1Create(@Valid @RequestBody ShopCreateFrom from) throws ProductException, UnauthorizedException {
        ShopCreateDto createDto = ShopCreateDto.builder().shopName(from.getName()).grade(from.getGrade()).build();
        String shopId = shopService.create(getUserId(), createDto);
        return StringUtils.isNotEmpty(shopId) ? ApiResult.ok(shopId) : ApiResult.result(ProductCode.SHOP_CREATE_ERR);
    }

    /**
     * 关店
     *
     * @param id 店铺Id
     * @return
     * @throws ProductException
     */
    @PutMapping("v1/shop/close/{id}")
    public ApiResult<String> v1Close(@PathVariable("id") String id) throws ProductException, UnauthorizedException {
        Optional.ofNullable(id).filter(StringUtils::isNotEmpty).orElseThrow(() -> new ProductException(CommCode.PARAMETER_EXCEPTION));
        shopService.close(getUserId(), id);
        return ApiResult.ok();
    }
}
