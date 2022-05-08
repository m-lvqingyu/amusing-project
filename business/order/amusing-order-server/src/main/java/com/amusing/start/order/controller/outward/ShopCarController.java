package com.amusing.start.order.controller.outward;

import com.amusing.start.code.CommCode;
import com.amusing.start.controller.BaseController;
import com.amusing.start.exception.UnauthorizedException;
import com.amusing.start.order.constant.OrderConstant;
import com.amusing.start.order.service.IShopCarService;
import com.amusing.start.result.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author ：lv.qingyu
 * @date ：2022/4/9 17:17
 */
@RestController
@RequestMapping("outward/shop/car")
public class ShopCarController extends BaseController {

    private final IShopCarService shopCarService;

    @Autowired
    public ShopCarController(HttpServletRequest request, IShopCarService shopCarService) {
        super(request);
        this.shopCarService = shopCarService;
    }

    /**
     * 购物车-添加商品
     *
     * @param productId  商品Id
     * @param productNum 商品数量
     * @return true:成功 false:失败
     * @throws UnauthorizedException
     */
    @PostMapping
    public ApiResult<?> operation(@RequestParam("id") String productId,
                                  @RequestParam("num") Integer productNum) throws UnauthorizedException {
        if (StringUtils.isEmpty(productId) || productNum == null || productNum <= OrderConstant.ZERO) {
            return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
        }
        return shopCarService.operation(getUserId(), productId, productNum) ?
                ApiResult.ok() : ApiResult.result(CommCode.FREQUENT_OPERATION_EXCEPTION);
    }

    /**
     * 购物车-获取购物车信息
     *
     * @return
     * @throws UnauthorizedException
     */
    @GetMapping
    public ApiResult<?> get() throws UnauthorizedException {
        Map<String, Integer> result = shopCarService.get(getUserId());
        return ApiResult.ok(result);
    }

    /**
     * 购物车-删除购物车信息
     *
     * @param productId 商品id
     * @return
     * @throws UnauthorizedException
     */
    @DeleteMapping
    public ApiResult<?> del(@RequestParam("id") String productId) throws UnauthorizedException {
        return shopCarService.del(getUserId(), productId) ?
                ApiResult.ok() : ApiResult.result(CommCode.FREQUENT_OPERATION_EXCEPTION);
    }

}
