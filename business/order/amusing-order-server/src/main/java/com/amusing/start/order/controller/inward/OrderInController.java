package com.amusing.start.order.controller.inward;

import com.amusing.start.client.api.OrderClient;
import com.amusing.start.client.input.AliPayAsyncNotifyInput;
import com.amusing.start.client.output.OrderDetailOutput;
import com.amusing.start.order.entity.pojo.OrderInfo;
import com.amusing.start.order.enums.OrderPaySubTypeEnum;
import com.amusing.start.order.service.OrderService;
import com.amusing.start.result.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/10/30
 */
@RestController
public class OrderInController implements OrderClient {

    private final OrderService orderService;

    @Autowired
    public OrderInController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ApiResult<OrderDetailOutput> orderDetail(String orderNo) {
        OrderInfo orderInfo = orderService.getByNo(orderNo);
        return ApiResult.ok(
                new OrderDetailOutput()
                        .setOrderNo(orderNo)
                        .setReserveId(orderInfo.getReserveId())
                        .setConsigneeId(orderInfo.getConsigneeId())
                        .setRealAmount(orderInfo.getRealAmount())
        );
    }

    @Override
    public ApiResult<Boolean> aliPayNotifySuccess(AliPayAsyncNotifyInput input) {
        return ApiResult.ok(
                orderService.aliPaySuccess(
                        OrderPaySubTypeEnum.PC.getKey(),
                        input.getNotifyId(),
                        input.getOrderNo(),
                        input.getTradeNo(),
                        input.getBuyerLogonId(),
                        input.getTotalAmount(),
                        input.getReceiptAmount(),
                        input.getBuyerPayAmount(),
                        input.getPointAmount(),
                        input.getInvoiceAmount(),
                        input.getMerchantDiscountAmount(),
                        input.getDiscountAmount(),
                        input.getGmtPayment()
                )
        );
    }

}
