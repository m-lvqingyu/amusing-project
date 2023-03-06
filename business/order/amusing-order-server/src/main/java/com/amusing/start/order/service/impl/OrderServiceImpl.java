package com.amusing.start.order.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.input.PayInput;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ShopCarOutput;
import com.amusing.start.client.output.AccountOutput;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.order.constant.order.OrderConstant;
import com.amusing.start.order.entity.dto.CreateDto;
import com.amusing.start.order.entity.vo.OrderDetailVo;
import com.amusing.start.order.entity.vo.OrderProductVo;
import com.amusing.start.order.enums.OrderStatus;
import com.amusing.start.order.enums.YesOrNo;
import com.amusing.start.order.mapper.OrderInfoMapper;
import com.amusing.start.order.mapper.OrderProductInfoMapper;
import com.amusing.start.order.entity.pojo.OrderInfo;
import com.amusing.start.order.entity.pojo.OrderProductInfo;
import com.amusing.start.order.service.IOrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
public class OrderServiceImpl implements IOrderService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private OrderProductInfoMapper orderProductInfoMapper;

    @Value("${order.worker}")
    private Long orderWorker;

    @Value("${order.dataCenter}")
    private Long orderDataCenter;

    private final ProductClient productClient;

    private final UserClient userClient;

    @Autowired
    public OrderServiceImpl(ProductClient productClient, UserClient userClient) {
        this.productClient = productClient;
        this.userClient = userClient;
    }

    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String create(String userId, CreateDto createDto) throws CustomException {
        AccountOutput userAccount = userClient.account(userId);
        List<ShopCarOutput> shopCarList = productClient.shopCar(userId);
        if (CollectionUtil.isEmpty(shopCarList)) {
            throw new CustomException(ErrorCode.SHOP_CAR_ERROR);
        }

        return doSaveOrder(createDto, userAccount, shopCarList);
    }

    @Override
    public OrderDetailVo getOrderDetail(String userId, String orderNo) throws CustomException {
        OrderInfo orderInfo = orderInfoMapper.getByNo(userId, orderNo);
        if (orderInfo == null) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }
        List<OrderProductInfo> productInfoList = orderProductInfoMapper.getByNo(orderNo);
        if (CollectionUtil.isEmpty(productInfoList)) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }
        List<OrderProductVo> productVoList = new ArrayList<>();
        for (OrderProductInfo productInfo : productInfoList) {
            OrderProductVo productVo = OrderProductVo.builder()
                    .orderNo(orderNo)
                    .shopId(productInfo.getShopId())
                    .shopName(productInfo.getShopName())
                    .productId(productInfo.getProductId())
                    .productName(productInfo.getProductName())
                    .priceId(productInfo.getPriceId())
                    .price(productInfo.getPrice())
                    .num(productInfo.getNum())
                    .amount(productInfo.getAmount())
                    .build();
            productVoList.add(productVo);
        }
        return OrderDetailVo.builder()
                .orderNo(orderNo)
                .reserveId(orderInfo.getReserveId())
                .consigneeId(orderInfo.getConsigneeId())
                .totalAmount(orderInfo.getTotalAmount())
                .freightAmount(orderInfo.getFreightAmount())
                .couponAmount(orderInfo.getCouponAmount())
                .activityAmount(orderInfo.getActivityAmount())
                .status(orderInfo.getStatus())
                .isFreight(orderInfo.getIsFreight())
                .isEvaluate(orderInfo.getIsEvaluate())
                .productList(productVoList)
                .build();
    }

    public String doSaveOrder(CreateDto createDto, AccountOutput account, List<ShopCarOutput> shopCarList) throws CustomException {
        Snowflake snowflake = IdUtil.getSnowflake(orderWorker, orderDataCenter);
        String orderNo = snowflake.nextIdStr();

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderProductInfo> infoList = new ArrayList<>();
        List<StockDeductionInput> stockDeductionInputs = new ArrayList<>();

        Iterator<ShopCarOutput> iterator = shopCarList.iterator();
        while (iterator.hasNext()) {
            ShopCarOutput output = iterator.next();
            BigDecimal price = output.getPrice();
            BigDecimal amount = price.multiply(new BigDecimal(output.getStock()));
            totalAmount = totalAmount.add(amount);
            OrderProductInfo productInfo = OrderProductInfo.builder()
                    .orderNo(orderNo)
                    .shopId(output.getShopId())
                    .shopName(output.getShopName())
                    .productId(output.getProductId())
                    .productName(output.getProductName())
                    .priceId(output.getPriceId())
                    .price(output.getPrice())
                    .num(output.getStock())
                    .amount(amount)
                    .build();
            stockDeductionInputs.add(StockDeductionInput.builder()
                    .productId(output.getProductId())
                    .productNum(output.getStock())
                    .build());
            infoList.add(productInfo);
        }

        BigDecimal userAmount = account.getMainAmount().add(account.getGiveAmount()).subtract(account.getFrozenAmount());
        if (userAmount.compareTo(totalAmount) < OrderConstant.ZERO) {
            throw new CustomException(ErrorCode.ACCOUNT_INSUFFICIENT);
        }

        Long currentTime = System.currentTimeMillis();
        OrderInfo orderInfo = OrderInfo.builder()
                .orderNo(orderNo)
                .reserveId(account.getUserId())
                .consigneeId(createDto.getConsigneeId())
                .freightAmount(BigDecimal.ZERO)
                .totalAmount(totalAmount)
                .couponAmount(BigDecimal.ZERO)
                .activityAmount(BigDecimal.ZERO)
                .status(OrderStatus.SCHEDULED.getKey())
                .isFreight(YesOrNo.YES.getKey())
                .isEvaluate(YesOrNo.NO.getKey())
                .createBy(account.getUserId())
                .createTime(currentTime)
                .updateBy(account.getUserId())
                .updateTime(currentTime)
                .build();

        orderInfoMapper.insert(orderInfo);
        for (OrderProductInfo orderProductInfo : infoList) {
            orderProductInfoMapper.insert(orderProductInfo);
        }
        Boolean payment = userClient.payment(PayInput.builder().userId(account.getUserId()).amount(totalAmount).build());
        if (payment == null || !payment) {
            throw new CustomException(ErrorCode.UNABLE_PROVIDE_SERVICE);
        }
        Boolean deductionStock = productClient.deductionStock(stockDeductionInputs);
        if (deductionStock == null || !deductionStock) {
            throw new CustomException(ErrorCode.UNABLE_PROVIDE_SERVICE);
        }
        return orderNo;
    }

}
