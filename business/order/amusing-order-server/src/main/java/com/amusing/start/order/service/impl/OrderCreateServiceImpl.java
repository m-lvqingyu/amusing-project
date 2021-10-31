package com.amusing.start.order.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.order.dto.OrderCreateDto;
import com.amusing.start.order.enums.OrderCode;
import com.amusing.start.order.enums.OrderStatus;
import com.amusing.start.order.enums.YesNo;
import com.amusing.start.order.exception.OrderException;
import com.amusing.start.order.listener.product.ReduceStockMqTemplate;
import com.amusing.start.order.mapper.OrderInfoMapper;
import com.amusing.start.order.mapper.OrderProductInfoMapper;
import com.amusing.start.order.mapper.OrderShopsInfoMapper;
import com.amusing.start.order.message.OrderCreateMsg;
import com.amusing.start.order.pojo.OrderInfo;
import com.amusing.start.order.service.IOrderCreateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 订单服务
 * @date 2021/10/15 16:44
 */
@Slf4j
@Service
public class OrderCreateServiceImpl implements IOrderCreateService {

    private final UserClient userClient;
    private final ProductClient productClient;
    private final OrderInfoMapper orderInfoMapper;
    private final ReduceStockMqTemplate reduceStockMqTemplate;
    private final OrderShopsInfoMapper orderShopsInfoMapper;
    private final OrderProductInfoMapper orderProductInfoMapper;

    private static final String REDUCE_STOCK_TOPIC = "reduce_stock";

    @Value("${order.worker}")
    private Long orderWorker;

    @Value("${order.dataCenter}")
    private Long orderDataCenter;

    @Autowired
    public OrderCreateServiceImpl(UserClient userClient,
                                  ProductClient productClient,
                                  OrderInfoMapper orderInfoMapper,
                                  ReduceStockMqTemplate reduceStockMqTemplate,
                                  OrderShopsInfoMapper orderShopsInfoMapper,
                                  OrderProductInfoMapper orderProductInfoMapper) {
        this.userClient = userClient;
        this.productClient = productClient;
        this.orderInfoMapper = orderInfoMapper;
        this.reduceStockMqTemplate = reduceStockMqTemplate;
        this.orderShopsInfoMapper = orderShopsInfoMapper;
        this.orderProductInfoMapper = orderProductInfoMapper;
    }


    @Override
    public String create(OrderCreateDto orderCreateDto) throws OrderException {
        // 预定人身份/金额校验
        String reserveUserId = orderCreateDto.getReserveUserId();
        UserAccountOutput userAccountOutput = userClient.account(reserveUserId);
        if (userAccountOutput == null) {
            log.warn("[Order]-[create]-ReserveUser does not exist! reserveUserId:{}, params:{}", reserveUserId, orderCreateDto);
            throw new OrderException(OrderCode.USER_NOT_FOUND);
        }
        // 获取商品信息及单价
        String shopsId = orderCreateDto.getShopsId();
        String priceId = orderCreateDto.getPriceId();
        String productId = orderCreateDto.getProductId();
        ProductOutput productOutput = productClient.get(shopsId, productId, priceId);
        if (productOutput == null) {
            log.warn("[Order]-[create]-Product does not exist! reserveUserId:{}, params:{}", reserveUserId, orderCreateDto);
            throw new OrderException(OrderCode.PRODUCT_NOT_FOUND);
        }
        // 保存订单
        BigDecimal price = productOutput.getPrice();
        OrderInfo orderInfo = build(price, orderCreateDto);
        int result = orderInfoMapper.insertSelective(orderInfo);
        if (result <= 0) {
            log.warn("[Order]-[create]-save order fail! reserveUserId:{}, params:{}", reserveUserId, orderCreateDto);
            throw new OrderException(OrderCode.ORDER_SAVE_FAIL);
        }

        String orderNo = orderInfo.getOrderNo();
        BigDecimal totalAmount = orderInfo.getTotalAmount();
        JSONObject object = new JSONObject();
        OrderCreateMsg msg = OrderCreateMsg.builder()
                .orderNo(orderNo)
                .shopsId(shopsId)
                .productId(productId)
                .priceId(priceId)
                .amount(totalAmount)
                .build();
        object.put("SimpOrderInfo", msg);
        Message message = MessageBuilder.withPayload(object.toJSONString()).build();
        reduceStockMqTemplate.sendMessageInTransaction(REDUCE_STOCK_TOPIC, message, null);
        return orderNo;
    }

    private OrderInfo build(BigDecimal price, OrderCreateDto orderCreateDto) {
        Snowflake snowflake = IdUtil.createSnowflake(orderWorker, orderDataCenter);
        String orderNo = snowflake.nextIdStr();
        Date currentTime = new Date();
        Integer productNum = orderCreateDto.getProductNum();
        BigDecimal totalAmount = price.multiply(new BigDecimal(productNum));
        return OrderInfo.builder()
                .orderNo(orderNo)
                .reserveUserId(orderCreateDto.getReserveUserId())
                .freightAmount(BigDecimal.ZERO)
                .totalAmount(totalAmount)
                .totalCouponAmount(BigDecimal.ZERO)
                .totalActivityAmount(BigDecimal.ZERO)
                .status(OrderStatus.SCHEDULED.getKey())
                .freeFreight(YesNo.YES.getKey())
                .isEvaluate(YesNo.NO.getKey())
                .createBy(orderCreateDto.getReserveUserId())
                .updateBy(orderCreateDto.getReserveUserId())
                .createTime(currentTime)
                .updateTime(currentTime)
                .build();
    }
}
