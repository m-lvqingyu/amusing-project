package com.amusing.start.order.controller.outward;

import com.amusing.start.controller.BaseController;
import com.amusing.start.exception.UnauthorizedException;
import com.amusing.start.order.dto.create.CreateDto;
import com.amusing.start.order.enums.OrderCode;
import com.amusing.start.order.exception.OrderException;
import com.amusing.start.order.from.create.CreateFrom;
import com.amusing.start.order.service.ICreateService;
import com.amusing.start.result.ApiResult;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Create By 2021/8/21
 *
 * @author lvqingyu
 */
@Slf4j
@RestController
@RequestMapping("/order/outward")
public class OrderInfoController extends BaseController {

    @Autowired
    public OrderInfoController(HttpServletRequest request, ICreateService orderCreateService) {
        super(request);
        this.orderCreateService = orderCreateService;
    }

    private final ICreateService orderCreateService;

    /**
     * 订单创建
     *
     * @param from  订单信息
     * @return
     */
    @PostMapping("create/v1")
    public ApiResult<String> create(@Valid @RequestBody CreateFrom from) throws OrderException, UnauthorizedException {
        CreateDto createDto = fromToDto(from);
        String orderId = "";
        try {
            orderId = orderCreateService.create(createDto);
        } catch (Exception e) {
            log.error("[Order]-[create]-msg:{}", Throwables.getStackTraceAsString(e));
        }
        return StringUtils.isEmpty(orderId) ? ApiResult.result(OrderCode.ORDER_SAVE_FAIL) : ApiResult.ok(orderId);
    }

    private CreateDto fromToDto(CreateFrom from) throws UnauthorizedException {
        return CreateDto.builder()
                .reserveId(getUserId())
                .consigneeId(from.getConsigneeId())
                .addressId(from.getAddressId())
                .build();
    }


}
