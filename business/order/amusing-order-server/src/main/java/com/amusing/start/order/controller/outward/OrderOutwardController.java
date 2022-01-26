package com.amusing.start.order.controller.outward;

import com.amusing.start.code.CommCode;
import com.amusing.start.controller.BaseController;
import com.amusing.start.order.dto.create.OrderCreateDto;
import com.amusing.start.order.dto.create.OrderProductDto;
import com.amusing.start.order.dto.create.OrderShopDto;
import com.amusing.start.order.enums.OrderCode;
import com.amusing.start.order.exception.OrderException;
import com.amusing.start.order.from.create.OrderCreateFrom;
import com.amusing.start.order.service.IOrderCreateService;
import com.amusing.start.order.service.IOrderService;
import com.amusing.start.order.vo.OrderDetailVO;
import com.amusing.start.result.ApiResult;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Create By 2021/8/21
 *
 * @author lvqingyu
 */
@Slf4j
@RestController
@RequestMapping("/order/outward")
public class OrderOutwardController extends BaseController {

    @Autowired
    public OrderOutwardController(HttpServletRequest request, IOrderService orderService, IOrderCreateService orderCreateService) {
        super(request);
        this.orderService = orderService;
        this.orderCreateService = orderCreateService;
    }

    private final IOrderService orderService;

    private final IOrderCreateService orderCreateService;

    /**
     * 根据ID，获取订单详情
     *
     * @param id 订单ID
     * @return
     * @throws OrderException
     */
    @GetMapping("v1/detail/{id}")
    public ApiResult<OrderDetailVO> detail(@PathVariable("id") String id) throws OrderException {
        // 请求参数校验
        Optional.ofNullable(id).filter(StringUtils::isNotEmpty).orElseThrow(() -> new OrderException(CommCode.PARAMETER_EXCEPTION));
        // 用户身份信息校验
        String userId = getUserId();
        Optional.ofNullable(userId).filter(StringUtils::isNotEmpty).orElseThrow(() -> new OrderException(CommCode.UNAUTHORIZED));
        // 获取订单信息
        OrderDetailVO orderDetailVO = orderService.get(id, userId);
        return ApiResult.ok(orderDetailVO);
    }

    /**
     * 创建用户
     *
     * @param from
     * @return
     */
    @PostMapping("v1/create")
    public ApiResult<String> create(@Valid @RequestBody OrderCreateFrom from) throws OrderException {
        String userId = getUserId();
        Optional.ofNullable(userId).filter(StringUtils::isNotEmpty).orElseThrow(() -> new OrderException(CommCode.UNAUTHORIZED));

        OrderCreateDto orderCreateDto = convert(userId, from);
        Optional.ofNullable(orderCreateDto).orElseThrow(() -> new OrderException(CommCode.PARAMETER_EXCEPTION));

        String orderId = null;
        try {
            orderId = orderCreateService.create(orderCreateDto);
        } catch (Exception e) {
            log.error("[order]-create err! userId:{},  param:{}, msg:{}", userId, from, Throwables.getStackTraceAsString(e));
        }
        Optional.ofNullable(orderId).filter(StringUtils::isNotEmpty).orElseThrow(() -> new OrderException(OrderCode.ORDER_SAVE_FAIL));
        return ApiResult.ok(orderId);
    }

    private OrderCreateDto convert(String reserveUserId, OrderCreateFrom from) {
        Optional<OrderCreateDto> optional = Optional.ofNullable(from).map(item -> {
            OrderCreateDto orderCreateDto = setUserProps(reserveUserId, item);
            List<OrderShopDto> shopDtoList = setShopProps(item);
            orderCreateDto.setShopDtoList(shopDtoList);
            return orderCreateDto;
        });
        return optional.orElse(null);
    }

    /**
     * 设置用户信息
     *
     * @param reserveUserId 下单人ID
     * @param item          订单信息
     * @return
     */
    private OrderCreateDto setUserProps(String reserveUserId, OrderCreateFrom item) {
        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setReserveUserId(reserveUserId);
        orderCreateDto.setReceiverUserId(item.getReceiverUserId());
        orderCreateDto.setReceiverAddressId(item.getReceiverAddressId());
        return orderCreateDto;
    }

    /**
     * 设置商品信息
     *
     * @param item 商品信息
     * @return
     */
    private List<OrderShopDto> setShopProps(OrderCreateFrom item) {
        List<OrderShopDto> shopDtoList = new ArrayList<>();
        item.getShopFromList().forEach(shopDetails -> {
            OrderShopDto orderShopDto = new OrderShopDto();
            orderShopDto.setShopsId(shopDetails.getShopsId());
            List<OrderProductDto> productDtoList = new ArrayList<>();
            shopDetails.getProductFromList().forEach(productDetail ->
                    productDtoList.add(new OrderProductDto(productDetail.getProductId(), productDetail.getProductNum())));
            orderShopDto.setProductDtoList(productDtoList);
            shopDtoList.add(orderShopDto);
        });
        return shopDtoList;
    }
}
