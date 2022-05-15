package com.amusing.start.order.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.order.constant.OrderConstant;
import com.amusing.start.order.dto.create.CreateDto;
import com.amusing.start.order.enums.OrderCode;
import com.amusing.start.order.enums.OrderStatus;
import com.amusing.start.order.enums.YesNo;
import com.amusing.start.order.exception.OrderException;
import com.amusing.start.order.manager.InwardManager;
import com.amusing.start.order.mapper.OrderInfoMapper;
import com.amusing.start.order.mapper.OrderProductInfoMapper;
import com.amusing.start.order.pojo.OrderInfo;
import com.amusing.start.order.pojo.OrderProductInfo;
import com.amusing.start.order.service.ICreateService;
import com.amusing.start.order.service.IShopCarService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
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
public class CreateServiceImpl implements ICreateService {

    private final InwardManager inwardManager;
    private final OrderInfoMapper orderInfoMapper;
    private final OrderProductInfoMapper orderProductInfoMapper;
    private final IShopCarService shopCarService;

    @Value("${order.worker}")
    private Long orderWorker;
    @Value("${order.dataCenter}")
    private Long orderDataCenter;

    @Autowired
    public CreateServiceImpl(InwardManager inwardManager,
                             OrderInfoMapper orderInfoMapper,
                             OrderProductInfoMapper orderProductInfoMapper,
                             IShopCarService shopCarService) {
        this.inwardManager = inwardManager;
        this.orderInfoMapper = orderInfoMapper;
        this.orderProductInfoMapper = orderProductInfoMapper;
        this.shopCarService = shopCarService;
    }

    /**
     * 创建订单
     *
     * @param createDto 订单信息
     * @return
     * @throws OrderException
     */
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String create(CreateDto createDto) throws OrderException {
        // 1.获取购物车信息
        Map<String, Integer> productMap = shopCarService.get(createDto.getReserveId());
        if (productMap == null || productMap.isEmpty()) {
            throw new OrderException(OrderCode.PRODUCT_NOT_FOUND);
        }
        // 2.判断库存是否足够
        Map<String, Long> productStock = inwardManager.productStock(productMap.keySet());
        if (productStock == null || productMap.size() != productStock.size()) {
            throw new OrderException(OrderCode.PRODUCT_NOT_FOUND);
        }
        Iterator<Map.Entry<String, Long>> iterator = productStock.entrySet().iterator();
        if (iterator.hasNext()) {
            Map.Entry<String, Long> next = iterator.next();
            Long value = next.getValue();
            if (value == null || value <= OrderConstant.ZERO) {
                throw new OrderException(OrderCode.PRODUCT_NOT_FOUND);
            }
        }
        // 3.获取预定人账户信息
        UserAccountOutput userAccount = inwardManager.accountDetail(createDto.getReserveId());
        Optional.ofNullable(userAccount).orElseThrow(() -> new OrderException(OrderCode.USER_NOT_FOUND));
        // 4.获取商品信息及单价
        Set<String> productIdSet = productMap.keySet();
        List<ProductOutput> productsDetails = inwardManager.productDetails(productIdSet);
        // 5.保存订单相关信息
        return doSaveOrder(createDto, userAccount, productMap, productsDetails);
    }

    /**
     * 保存订单
     *
     * @param createDto       订单信息
     * @param userAccount     账户详情
     * @param productsDetails 商品详情
     * @return
     * @throws OrderException
     */
    public String doSaveOrder(CreateDto createDto, UserAccountOutput userAccount, Map<String, Integer> productMap, List<ProductOutput> productsDetails) throws OrderException {
        Snowflake snowflake = IdUtil.getSnowflake(orderWorker, orderDataCenter);
        String orderNo = snowflake.nextIdStr();
        Iterator<Map.Entry<String, Integer>> iterator = productMap.entrySet().iterator();
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderProductInfo> infoList = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> next = iterator.next();
            String productId = next.getKey();
            Integer productNum = next.getValue();
            for (ProductOutput output : productsDetails) {
                if (productId.equals(output.getProductId())) {
                    BigDecimal price = output.getPrice();
                    BigDecimal amount = price.multiply(new BigDecimal(productNum));
                    totalAmount = totalAmount.add(amount);
                    OrderProductInfo productInfo = OrderProductInfo.builder()
                            .orderNo(orderNo)
                            .shopId(output.getShopId())
                            .shopName(output.getShopName())
                            .productId(output.getProductId())
                            .productName(output.getProductName())
                            .priceId(output.getPriceId())
                            .price(output.getPrice())
                            .num(productNum)
                            .amount(amount)
                            .build();
                    infoList.add(productInfo);
                }
            }
        }
        Long currentTime = System.currentTimeMillis();
        OrderInfo orderInfo = OrderInfo.builder()
                .orderNo(orderNo)
                .reserveId(createDto.getReserveId())
                .consigneeId(createDto.getConsigneeId())
                .freightAmount(BigDecimal.ZERO)
                .totalAmount(totalAmount)
                .couponAmount(BigDecimal.ZERO)
                .activityAmount(BigDecimal.ZERO)
                .status(OrderStatus.SCHEDULED.getKey())
                .isFreight(YesNo.YES.getKey())
                .isEvaluate(YesNo.NO.getKey())
                .createBy(createDto.getReserveId())
                .createTime(currentTime)
                .updateBy(createDto.getReserveId())
                .updateTime(currentTime)
                .build();
        BigDecimal userAmount = userAccount.getMainAmount().add(userAccount.getGiveAmount()).subtract(userAccount.getFrozenAmount());
        if (userAmount.compareTo(totalAmount) < OrderConstant.ZERO) {
            throw new OrderException(OrderCode.INSUFFICIENT_BALANCE);
        }

        // 保存订单-口库存-扣余额
        orderInfoMapper.insert(orderInfo);
        for (OrderProductInfo orderProductInfo : infoList) {
            orderProductInfoMapper.insert(orderProductInfo);
        }
        Boolean result = inwardManager.mainSettlement(orderInfo.getReserveId(), totalAmount);
        if (!result) {
            throw new OrderException(OrderCode.UNABLE_PROVIDE_SERVICE);
        }
        result = inwardManager.deductionStock(productMap);
        if (!result) {
            throw new OrderException(OrderCode.UNABLE_PROVIDE_SERVICE);
        }
        return orderNo;
    }

}
