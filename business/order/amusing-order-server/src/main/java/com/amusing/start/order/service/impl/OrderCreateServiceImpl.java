package com.amusing.start.order.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        List<OrderShopDto> shopDtoList = orderCreateDto.getShopDtoList();
        List<ShopOutput> shopList = inwardServletManager.getProductDetails(reserveUserId, shopDtoList);
        Optional.ofNullable(shopList).filter(i -> i.size() > OrderConstant.ZERO).orElseThrow(() -> new OrderException(OrderCode.PRODUCT_NOT_FOUND));

        return doSaveOrder(orderCreateDto, userAccount, shopList);
    }

    public String doSaveOrder(OrderCreateDto orderCreateDto, UserAccountOutput userAccount, List<ShopOutput> shopList) throws OrderException {
        Snowflake snowflake = IdUtil.getSnowflake(orderWorker, orderDataCenter);
        String orderNo = snowflake.nextIdStr();
        List<OrderShopsInfo> shopsInfoList = new ArrayList<>();
        List<OrderProductInfo> productInfoList = new ArrayList<>();
        orderCreateDto.getShopDtoList().forEach(i -> {
            String shopsId = i.getShopsId();
            i.getProductDtoList().forEach(x -> {
                String productId = x.getProductId();
                Integer productNum = x.getProductNum();
                shopList.forEach(shop -> {
                    String shopId = shop.getShopId();
                    if (shopsId.equals(shopId)) {
                        OrderShopsInfo shopsInfo = OrderShopsInfo.builder().orderNo(orderNo).shopsId(shopsId).shopsName(shop.getShopName()).build();
                        shopsInfoList.add(shopsInfo);
                        shop.getProductList().forEach(product -> {
                            String currentProductId = product.getProductId();
                            if (productId.equals(currentProductId)) {
                                BigDecimal amount = product.getPrice().multiply(new BigDecimal(productNum));
                                OrderProductInfo productInfo = OrderProductInfo.builder()
                                        .orderNo(orderNo)
                                        .shopsId(shopsId)
                                        .productId(productId)
                                        .productName(product.getProductName())
                                        .productNum(productNum)
                                        .priceId(product.getPriceId())
                                        .productPrice(product.getPrice())
                                        .amount(amount)
                                        .build();
                                productInfoList.add(productInfo);
                            }
                        });
                    }
                });
            });
        });

        BigDecimal totalAmount = productInfoList.stream().map(OrderProductInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        Date currentTime = new Date();
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
        orderShopsInfoMapper.batchInsertSelective(shopsInfoList);
        orderProductInfoMapper.batchInsertSelective(productInfoList);
        boolean result = inwardServletManager.mainSettlement(orderInfo.getReserveUserId(), orderInfo.getTotalAmount());
        if (!result) {
            throw new RuntimeException(OrderCode.UNABLE_PROVIDE_SERVICE.value());
        }
        result = inwardServletManager.deductionStock(productInfoList);
        if (!result) {
            throw new RuntimeException(OrderCode.UNABLE_PROVIDE_SERVICE.value());
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
