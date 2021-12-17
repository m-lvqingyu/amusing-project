package com.amusing.start.order.service.impl;

import com.amusing.start.client.output.ShopOutput;
import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.order.constant.OrderConstant;
import com.amusing.start.order.dto.create.OrderCreateDto;
import com.amusing.start.order.dto.create.OrderShopDto;
import com.amusing.start.order.enums.OrderCode;
import com.amusing.start.order.exception.OrderException;
import com.amusing.start.order.manager.InwardServletManager;
import com.amusing.start.order.mapper.OrderInfoMapper;
import com.amusing.start.order.mapper.OrderProductInfoMapper;
import com.amusing.start.order.mapper.OrderShopsInfoMapper;
import com.amusing.start.order.service.IOrderCreateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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


        return "";
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

}
