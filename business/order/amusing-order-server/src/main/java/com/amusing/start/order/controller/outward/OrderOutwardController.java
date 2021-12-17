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
import org.springframework.beans.BeanUtils;
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
    @GetMapping("/v1/{id}")
    public ApiResult<OrderDetailVO> get(@PathVariable("id") String id) throws OrderException {
        Optional.ofNullable(id).filter(i -> StringUtils.isNotEmpty(id)).orElseThrow(() -> new OrderException(CommCode.PARAMETER_EXCEPTION));

        String userId = getUserId();
        Optional.ofNullable(userId).filter(i -> StringUtils.isNotEmpty(userId)).orElseThrow(() -> new OrderException(CommCode.UNAUTHORIZED));

        OrderDetailVO orderDetailVO = orderService.get(id, userId);
        return ApiResult.ok(orderDetailVO);
    }

    /**
     * 创建用户
     *
     * @param from
     * @return
     */
    @PostMapping
    public ApiResult<String> create(@Valid @RequestBody OrderCreateFrom from) throws OrderException {
        String userId = getUserId();
        Optional.ofNullable(userId).filter(StringUtils::isNotEmpty).orElseThrow(() -> new OrderException(CommCode.UNAUTHORIZED));

        Optional<OrderCreateDto> optional = Optional.ofNullable(from).map(i -> {
            OrderCreateDto orderCreateDto = new OrderCreateDto();
            BeanUtils.copyProperties(i, orderCreateDto);
            List<OrderShopDto> shopDtoList = new ArrayList<>();

            i.getShopFromList().forEach(x -> {
                String shopsId = x.getShopsId();
                OrderShopDto orderShopDto = new OrderShopDto();
                orderShopDto.setShopsId(shopsId);
                List<OrderProductDto> productDtoList = new ArrayList<>();

                x.getProductFromList().forEach(c -> {
                    productDtoList.add(new OrderProductDto(c.getProductId(), c.getProductNum()));
                });
                orderShopDto.setProductDtoList(productDtoList);
                shopDtoList.add(orderShopDto);
            });
            orderCreateDto.setShopDtoList(shopDtoList);
            return orderCreateDto;
        });

        if (!optional.isPresent()) {
            return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
        }

        OrderCreateDto orderCreateDto = optional.get();
        orderCreateDto.setReserveUserId(userId);
        String orderId = null;
        try {
            orderId = orderCreateService.create(orderCreateDto);
        } catch (Exception e) {
            log.error("[order]-create err! userId:{},  param:{}, msg:{}", userId, from, Throwables.getStackTraceAsString(e));
        }
        Optional.ofNullable(orderId).filter(StringUtils::isNotEmpty).orElseThrow(() -> new OrderException(OrderCode.ORDER_SAVE_FAIL));
        return ApiResult.ok(orderId);
    }

}
