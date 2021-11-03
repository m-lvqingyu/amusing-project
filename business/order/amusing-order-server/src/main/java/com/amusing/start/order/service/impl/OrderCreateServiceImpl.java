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
import com.amusing.start.order.listener.transactional.product.ReduceStockMqTemplate;
import com.amusing.start.order.mapper.OrderInfoMapper;
import com.amusing.start.order.mapper.OrderProductInfoMapper;
import com.amusing.start.order.mapper.OrderShopsInfoMapper;
import com.amusing.start.order.message.OrderCreateMsg;
import com.amusing.start.order.pojo.OrderInfo;
import com.amusing.start.order.pojo.OrderProductInfo;
import com.amusing.start.order.pojo.OrderShopsInfo;
import com.amusing.start.order.service.IOrderCreateService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private static final String MESSAGE_ID_KEY = "msgUid";

    private static final String REDUCE_STOCK_TOPIC = "reduce_stock";

    private static final String REDUCE_STOCK_MSG_ID_PREFIX = "reduce_stock_";

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

    /**
     * 创建订单 创建订单
     * ------> 扣减库存MQ
     *
     * @param orderCreateDto
     * @return
     * @throws OrderException
     */
    @Override
    public String create(OrderCreateDto orderCreateDto) throws OrderException {
        String reserveUserId = orderCreateDto.getReserveUserId();

        // 获取预定人账户信息
        UserAccountOutput userAccountOutput = getUserAccountDetails(reserveUserId);

        // 获取商品信息及单价
        ProductOutput productOutput = getProductDetails(reserveUserId, orderCreateDto);

        // 判断预定人账户金额是否足够
        BigDecimal price = productOutput.getPrice();
        Integer productNum = orderCreateDto.getProductNum();
        BigDecimal totalAmount = price.multiply(new BigDecimal(productNum));
        checkoutOrderAccount(totalAmount, userAccountOutput);

        // 保存订单
        OrderInfo orderInfo;
        try {
            orderInfo = saveOrders(price, totalAmount, orderCreateDto);
        } catch (Exception e) {
            log.error("[Order]-[create]-saveOrder err！userId:{}, msg:{}", reserveUserId, Throwables.getStackTraceAsString(e));
            throw new OrderException(OrderCode.UNABLE_PROVIDE_SERVICE);
        }

        // 发送扣减库存消息
        String orderNo = orderInfo.getOrderNo();
        boolean result = sendReduceStockMsg(orderNo, totalAmount, orderCreateDto);
        if (!result) {
            cancelOrder(orderNo);
            throw new OrderException(OrderCode.UNABLE_PROVIDE_SERVICE);
        }
        return orderNo;
    }

    /**
     * 获取用户账户信息
     *
     * @param reserveUserId 预定人ID
     * @return
     * @throws OrderException
     */
    private UserAccountOutput getUserAccountDetails(String reserveUserId) throws OrderException {
        UserAccountOutput userAccountOutput = null;
        try {
            userAccountOutput = userClient.account(reserveUserId);
        } catch (Exception e) {
            log.error("[Order]-[create]-getUserAccountDetails err! reserveUserId:{}, msg:{}", reserveUserId, Throwables.getStackTraceAsString(e));
        }
        if (userAccountOutput == null) {
            log.warn("[Order]-[create]-userAccount does not exist! reserveUserId:{}", reserveUserId);
            throw new OrderException(OrderCode.USER_NOT_FOUND);
        }
        return userAccountOutput;
    }

    /**
     * 获取商品信息
     *
     * @param reserveUserId  预定人ID
     * @param orderCreateDto
     * @return
     * @throws OrderException
     */
    private ProductOutput getProductDetails(String reserveUserId, OrderCreateDto orderCreateDto) throws OrderException {
        String shopsId = orderCreateDto.getShopsId();
        String productId = orderCreateDto.getProductId();
        String priceId = orderCreateDto.getPriceId();
        ProductOutput productOutput = null;
        try {
            productOutput = productClient.get(shopsId, productId, priceId);
        } catch (Exception e) {
            log.error("[Order]-[create]-getProductDetails err! reserveUserId:{}, shopsId:{}, productId:{}, priceId:{}, msg:{}",
                    reserveUserId,
                    shopsId,
                    productId,
                    priceId,
                    Throwables.getStackTraceAsString(e));
        }
        if (productOutput == null) {
            log.warn("[Order]-[create]-Product does not exist!reserveUserId:{}, shopsId:{}, productId:{}, priceId:{}",
                    reserveUserId,
                    shopsId,
                    productId,
                    priceId);
            throw new OrderException(OrderCode.PRODUCT_NOT_FOUND);
        }
        return productOutput;
    }

    /**
     * 判断预定人账户金额是否足够
     *
     * @param totalAmount 订单总金额
     * @param userAccount 用户账户余额信息
     * @throws OrderException
     */
    private void checkoutOrderAccount(BigDecimal totalAmount, UserAccountOutput userAccount) throws OrderException {
        BigDecimal mainAmount = userAccount.getMainAmount();
        if (mainAmount == null) {
            mainAmount = BigDecimal.ZERO;
        }
        BigDecimal giveAmount = userAccount.getGiveAmount();
        if (giveAmount == null) {
            giveAmount = BigDecimal.ZERO;
        }
        BigDecimal frozenAmount = userAccount.getFrozenAmount();
        if (frozenAmount == null) {
            frozenAmount = BigDecimal.ZERO;
        }
        BigDecimal userAmount = mainAmount.add(giveAmount).subtract(frozenAmount);
        if (userAmount.compareTo(totalAmount) < -1) {
            String userId = userAccount.getUserId();
            log.warn("[Order]-[create]-userAmount insufficient balance！userId:{}, userAmount:{}, totalAmount:{}", userId, userAmount, totalAmount);
            throw new OrderException(OrderCode.INSUFFICIENT_BALANCE);
        }
    }

    /**
     * 保存订单相关信息
     *
     * @param price          商品单价
     * @param totalAmount    订单总金额
     * @param orderCreateDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderInfo saveOrders(BigDecimal price, BigDecimal totalAmount, OrderCreateDto orderCreateDto) {
        Snowflake snowflake = IdUtil.createSnowflake(orderWorker, orderDataCenter);
        String orderNo = snowflake.nextIdStr();
        // 保存订单商铺信息
        OrderShopsInfo shopsInfo = buildOrderShopsInfo(orderNo, orderCreateDto.getShopsId());
        orderShopsInfoMapper.insertSelective(shopsInfo);
        // 保存订单商品信息
        OrderProductInfo orderProductInfo = buildOrderProductInfo(orderNo, price, totalAmount, orderCreateDto);
        orderProductInfoMapper.insertSelective(orderProductInfo);
        // 保存订单信息
        OrderInfo orderInfo = buildOrderInfo(orderNo, totalAmount, orderCreateDto);
        orderInfoMapper.insertSelective(orderInfo);
        return orderInfo;
    }

    /**
     * 发送扣减库存MQ消息
     *
     * @param orderNo        订单编号
     * @param totalAmount    订单金额
     * @param orderCreateDto
     * @return
     */
    public boolean sendReduceStockMsg(String orderNo, BigDecimal totalAmount, OrderCreateDto orderCreateDto) {
        JSONObject object = new JSONObject();
        OrderCreateMsg msg = OrderCreateMsg.builder()
                .orderNo(orderNo)
                .shopsId(orderCreateDto.getShopsId())
                .productId(orderCreateDto.getProductId())
                .priceId(orderCreateDto.getPriceId())
                .amount(totalAmount)
                .build();
        object.put("SimpleOrderInfo", msg);
        String msgId = REDUCE_STOCK_MSG_ID_PREFIX + IdUtil.simpleUUID();
        Message message = MessageBuilder.withPayload(object.toJSONString()).setHeader(MESSAGE_ID_KEY, msgId).build();
        try {
            reduceStockMqTemplate.sendMessageInTransaction(REDUCE_STOCK_TOPIC, message, null);
            return true;
        } catch (Exception e) {
            log.error("[Order]-[create]-sendReduceStockMsg err! orderNo:{}, msgId:{}, msg:{}", orderNo, msgId, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    /**
     * 取消订单
     *
     * @param orderNo 订单编号
     */
    public void cancelOrder(String orderNo) {
        try {
            orderInfoMapper.updateOrderStatus(orderNo, OrderStatus.CANCEL.getKey());
        } catch (Exception e) {
            log.error("[Order]-[create]-cancelOrder err! orderNo:{}, msg:{}", orderNo, Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 构建订单商铺信息
     *
     * @param orderNo 订单编号
     * @param shopsId 商铺ID
     * @return
     */
    private OrderShopsInfo buildOrderShopsInfo(String orderNo, String shopsId) {
        return OrderShopsInfo.builder().orderNo(orderNo).shopsId(shopsId).build();
    }

    /**
     * 构建订单商品信息
     *
     * @param orderNo        订单编号
     * @param price          商品单价
     * @param totalAmount    订单总金额
     * @param orderCreateDto
     * @return
     */
    private OrderProductInfo buildOrderProductInfo(String orderNo, BigDecimal price, BigDecimal totalAmount, OrderCreateDto orderCreateDto) {
        return OrderProductInfo.builder()
                .orderNo(orderNo)
                .shopsId(orderCreateDto.getShopsId())
                .productId(orderCreateDto.getProductId())
                .productPrice(price)
                .productNum(orderCreateDto.getProductNum())
                .amount(totalAmount)
                .couponId("")
                .couponAmount(BigDecimal.ZERO)
                .activityId("")
                .activityAmount(BigDecimal.ZERO)
                .build();
    }

    /**
     * 构建订单信息
     *
     * @param orderNo        订单编号
     * @param totalAmount    订单总金额
     * @param orderCreateDto
     * @return
     */
    private OrderInfo buildOrderInfo(String orderNo, BigDecimal totalAmount, OrderCreateDto orderCreateDto) {
        Date currentTime = new Date();
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
