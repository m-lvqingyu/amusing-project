package com.amusing.start.order.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.client.output.ShopOutput;
import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.order.constant.OrderConstant;
import com.amusing.start.order.dto.create.OrderCreateDto;
import com.amusing.start.order.dto.create.OrderShopDto;
import com.amusing.start.order.enums.OrderCode;
import com.amusing.start.order.enums.OrderStatus;
import com.amusing.start.order.enums.YesNo;
import com.amusing.start.order.exception.OrderException;
import com.amusing.start.order.manager.InwardServletManager;
import com.amusing.start.order.mapper.OrderInfoMapper;
import com.amusing.start.order.mapper.OrderProductInfoMapper;
import com.amusing.start.order.mapper.OrderShopsInfoMapper;
import com.amusing.start.order.pojo.OrderInfo;
import com.amusing.start.order.pojo.OrderProductInfo;
import com.amusing.start.order.pojo.OrderShopsInfo;
import com.amusing.start.order.service.IOrderCreateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 订单服务
 * @date 2021/10/15 16:44
 */
@Slf4j
@Service
public class OrderCreateServiceImpl implements IOrderCreateService {

    private final InwardServletManager inwardServletManager;
    private final OrderInfoMapper orderInfoMapper;
    private final OrderShopsInfoMapper orderShopsInfoMapper;
    private final OrderProductInfoMapper orderProductInfoMapper;

    @Value("${order.worker}")
    private Long orderWorker;
    @Value("${order.dataCenter}")
    private Long orderDataCenter;

    @Autowired
    public OrderCreateServiceImpl(InwardServletManager inwardServletManager,
                                  OrderInfoMapper orderInfoMapper,
                                  OrderShopsInfoMapper orderShopsInfoMapper,
                                  OrderProductInfoMapper orderProductInfoMapper) {
        this.inwardServletManager = inwardServletManager;
        this.orderInfoMapper = orderInfoMapper;
        this.orderShopsInfoMapper = orderShopsInfoMapper;
        this.orderProductInfoMapper = orderProductInfoMapper;
    }

    /**
     * 创建订单
     *
     * @param orderCreateDto 订单信息
     * @return
     * @throws OrderException
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.BASE)
    @Override
    public String create(OrderCreateDto orderCreateDto) throws OrderException {
        String reserveUserId = orderCreateDto.getReserveUserId();
        // 获取预定人账户信息
        UserAccountOutput userAccount = inwardServletManager.getUserAccountDetails(reserveUserId);
        Optional.ofNullable(userAccount).orElseThrow(() -> new OrderException(OrderCode.USER_NOT_FOUND));

        // 获取商品信息及单价
        Set<String> shopIds = new HashSet<>();
        Set<String> productIds = new HashSet<>();
        List<OrderShopDto> shopDtoList = orderCreateDto.getShopDtoList();
        shopDtoList.forEach(i -> {
            shopIds.add(i.getShopsId());
            i.getProductDtoList().forEach(product -> {
                productIds.add(product.getProductId());
            });
        });
        Map<String, ShopOutput> shopDetails = inwardServletManager.getShopDetails(reserveUserId, shopIds);
        Optional.ofNullable(shopDetails).filter(i -> i.size() > OrderConstant.ZERO).orElseThrow(() -> new OrderException(OrderCode.PRODUCT_NOT_FOUND));
        Map<String, ProductOutput> productDetails = inwardServletManager.getProductDetails(reserveUserId, productIds);
        Optional.ofNullable(productDetails).filter(i -> i.size() > OrderConstant.ZERO).orElseThrow(() -> new OrderException(OrderCode.PRODUCT_NOT_FOUND));

        return doSaveOrder(orderCreateDto, userAccount, shopDetails, productDetails);
    }

    public String doSaveOrder(OrderCreateDto orderCreateDto,
                              UserAccountOutput userAccount,
                              Map<String, ShopOutput> shopDetails,
                              Map<String, ProductOutput> productDetails) throws OrderException {
        Snowflake snowflake = IdUtil.getSnowflake(orderWorker, orderDataCenter);
        String orderNo = snowflake.nextIdStr();
        List<OrderShopsInfo> shopsInfoList = new ArrayList<>();
        List<OrderProductInfo> productInfoList = new ArrayList<>();
        orderCreateDto.getShopDtoList().forEach(i -> {
            String shopsId = i.getShopsId();
            ShopOutput shopOutput = shopDetails.get(shopsId);
            OrderShopsInfo shopsInfo = OrderShopsInfo.builder().orderNo(orderNo).shopsId(shopOutput.getShopId()).shopsName(shopOutput.getShopName()).build();
            shopsInfoList.add(shopsInfo);
            i.getProductDtoList().forEach(product -> {
                String productId = product.getProductId();
                Integer productNum = product.getProductNum();
                ProductOutput productOutput = productDetails.get(productId);
                BigDecimal price = productOutput.getPrice();
                BigDecimal amount = new BigDecimal(productNum).multiply(price);
                OrderProductInfo productInfo = OrderProductInfo.builder()
                        .orderNo(orderNo)
                        .shopsId(shopsId)
                        .productId(productId)
                        .productName(productOutput.getProductName())
                        .priceId(productOutput.getPriceId())
                        .productPrice(price)
                        .productNum(productNum)
                        .amount(amount)
                        .build();
                productInfoList.add(productInfo);
            });

        });

        BigDecimal totalAmount = productInfoList.stream().map(OrderProductInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        Long currentTime = System.currentTimeMillis();
        OrderInfo orderInfo = OrderInfo.builder()
                .orderNo(orderNo)
                .reserveUserId(orderCreateDto.getReserveUserId())
                .receivingUserId(orderCreateDto.getReceiverUserId())
                .freightAmount(BigDecimal.ZERO)
                .totalAmount(totalAmount)
                .totalCouponAmount(BigDecimal.ZERO)
                .totalActivityAmount(BigDecimal.ZERO)
                .status(OrderStatus.SCHEDULED.getKey())
                .freeFreight(YesNo.YES.getKey())
                .isEvaluate(YesNo.NO.getKey())
                .createBy(orderCreateDto.getReserveUserId())
                .createTime(currentTime)
                .updateBy(orderCreateDto.getReserveUserId())
                .updateTime(currentTime)
                .build();
        checkoutOrderAccount(totalAmount, userAccount);
        handleSaveOrder(orderInfo, shopsInfoList, productInfoList);
        return orderNo;
    }

    public void handleSaveOrder(OrderInfo orderInfo, List<OrderShopsInfo> shopsInfoList, List<OrderProductInfo> productInfoList) throws OrderException {
        orderInfoMapper.insertSelective(orderInfo);
        for (OrderShopsInfo orderShopsInfo : shopsInfoList) {
            orderShopsInfoMapper.insertSelective(orderShopsInfo);
        }
        for (OrderProductInfo orderProductInfo : productInfoList) {
            orderProductInfoMapper.insertSelective(orderProductInfo);
        }
        boolean result = inwardServletManager.mainSettlement(orderInfo.getReserveUserId(), orderInfo.getTotalAmount());
        if (!result) {
            throw new OrderException(OrderCode.UNABLE_PROVIDE_SERVICE);
        }
        result = inwardServletManager.deductionStock(productInfoList);
        if (!result) {
            throw new OrderException(OrderCode.UNABLE_PROVIDE_SERVICE);
        }
    }


    /**
     * 判断预定人账户金额是否足够
     *
     * @param totalAmount 订单总金额
     * @param userAccount 用户账户余额信息
     * @throws OrderException
     */
    private void checkoutOrderAccount(BigDecimal totalAmount, UserAccountOutput userAccount) throws OrderException {
        BigDecimal mainAmount = Optional.ofNullable(userAccount)
                .map(UserAccountOutput::getMainAmount).orElse(BigDecimal.ZERO);

        BigDecimal giveAmount = Optional.ofNullable(userAccount)
                .map(UserAccountOutput::getGiveAmount).orElse(BigDecimal.ZERO);

        BigDecimal frozenAmount = Optional.ofNullable(userAccount)
                .map(UserAccountOutput::getFrozenAmount).orElse(BigDecimal.ZERO);

        BigDecimal userAmount = mainAmount.add(giveAmount).subtract(frozenAmount);

        if (userAmount.compareTo(totalAmount) < OrderConstant.ZERO) {
            String userId = Optional.ofNullable(userAccount).map(UserAccountOutput::getUserId).orElse("");
            log.warn("[order]-create userAmount insufficient balance！userId:{}, userAmount:{}, totalAmount:{}",
                    userId,
                    userAmount,
                    totalAmount);
            throw new OrderException(OrderCode.INSUFFICIENT_BALANCE);
        }
    }

}
