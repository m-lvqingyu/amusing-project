package com.amusing.start.order.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.amusing.start.client.input.ShopProductIdInput;
import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.order.constant.OrderConstant;
import com.amusing.start.order.dto.create.OrderCreateDto;
import com.amusing.start.order.dto.create.OrderProductDto;
import com.amusing.start.order.dto.create.OrderShopDto;
import com.amusing.start.order.enums.OrderCode;
import com.amusing.start.order.enums.OrderStatus;
import com.amusing.start.order.enums.YesNo;
import com.amusing.start.order.exception.OrderException;
import com.amusing.start.order.manager.InwardServletManager;
import com.amusing.start.order.mapper.OrderInfoMapper;
import com.amusing.start.order.mapper.OrderProductInfoMapper;
import com.amusing.start.order.pojo.OrderInfo;
import com.amusing.start.order.pojo.OrderProductInfo;
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
    private final OrderProductInfoMapper orderProductInfoMapper;

    @Value("${order.worker}")
    private Long orderWorker;
    @Value("${order.dataCenter}")
    private Long orderDataCenter;

    @Autowired
    public OrderCreateServiceImpl(InwardServletManager inwardServletManager,
                                  OrderInfoMapper orderInfoMapper,
                                  OrderProductInfoMapper orderProductInfoMapper) {
        this.inwardServletManager = inwardServletManager;
        this.orderInfoMapper = orderInfoMapper;
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
        // 1.获取预定人账户信息
        UserAccountOutput userAccount = inwardServletManager.getUserAccountDetails(orderCreateDto.getReserveUserId());
        Optional.ofNullable(userAccount).orElseThrow(() -> new OrderException(OrderCode.USER_NOT_FOUND));
        // 2.获取商品信息及单价
        List<ProductOutput> productsDetails = getProductsDetails(orderCreateDto.getShopDtoList());
        Optional.ofNullable(productsDetails).filter(i -> i.size() > OrderConstant.ZERO).orElseThrow(() -> new OrderException(OrderCode.PRODUCT_NOT_FOUND));
        // 3.保存订单相关信息
        return doSaveOrder(orderCreateDto, userAccount, productsDetails);
    }

    /**
     * 保存订单
     *
     * @param orderCreateDto  订单信息
     * @param userAccount     账户详情
     * @param productsDetails 商品详情
     * @return
     * @throws OrderException
     */
    public String doSaveOrder(OrderCreateDto orderCreateDto, UserAccountOutput userAccount, List<ProductOutput> productsDetails) throws OrderException {
        Snowflake snowflake = IdUtil.getSnowflake(orderWorker, orderDataCenter);
        String orderNo = snowflake.nextIdStr();
        // 获得订单-商品关系集合
        List<OrderShopDto> shopDtoList = orderCreateDto.getShopDtoList();
        List<OrderProductInfo> productInfoList = buildOrderProductInfo(orderNo, shopDtoList, productsDetails);
        // 获得订单总金额
        BigDecimal totalAmount = productInfoList.stream().map(OrderProductInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 判断账户金额是否足够
        checkoutOrderAccount(totalAmount, userAccount);
        // 构建订单信息
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
        // 保存订单-口库存-扣余额
        orderInfoMapper.insertSelective(orderInfo);
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
        return orderNo;
    }

    private List<OrderProductInfo> buildOrderProductInfo(String orderNo, List<OrderShopDto> shopDtoList, List<ProductOutput> productsDetails) throws OrderException {
        List<OrderProductInfo> productInfoList = new ArrayList<>();
        for (OrderShopDto shopDto : shopDtoList) {
            String shopsId = shopDto.getShopsId();
            List<OrderProductDto> productDtoList = shopDto.getProductDtoList();
            for (OrderProductDto productDto : productDtoList) {
                String productId = productDto.getProductId();
                ProductOutput currentProductDetail = null;
                for (ProductOutput output : productsDetails) {
                    String currentShopId = output.getShopId();
                    String currentProductId = output.getProductId();
                    if (shopsId.equals(currentShopId) && productId.equals(currentProductId)) {
                        currentProductDetail = output;
                    }
                }
                if (currentProductDetail == null) {
                    throw new OrderException(OrderCode.PRODUCT_NOT_FOUND);
                }
                Integer productNum = productDto.getProductNum();
                if (productNum == null || productNum <= OrderConstant.ZERO) {
                    throw new OrderException(CommCode.PARAMETER_EXCEPTION);
                }
                BigDecimal price = currentProductDetail.getPrice();
                if (price == null || price.compareTo(BigDecimal.ZERO) < OrderConstant.ZERO) {
                    throw new OrderException(OrderCode.PRODUCT_NOT_FOUND);
                }
                BigDecimal amount = new BigDecimal(productNum).multiply(price);
                OrderProductInfo productInfo = OrderProductInfo.builder()
                        .orderNo(orderNo)
                        .shopsId(shopsId)
                        .shopsName(currentProductDetail.getShopName())
                        .productId(productId)
                        .productName(currentProductDetail.getProductName())
                        .priceId(currentProductDetail.getPriceId())
                        .productPrice(price)
                        .productNum(productNum)
                        .amount(amount)
                        .build();
                productInfoList.add(productInfo);
            }
        }
        return productInfoList;
    }

    /**
     * 获取商品信息
     *
     * @param shopDtoList 商品ID
     * @return
     */
    private List<ProductOutput> getProductsDetails(List<OrderShopDto> shopDtoList) {
        List<ShopProductIdInput> inputList = new ArrayList<>();
        shopDtoList.forEach(i -> {
            String shopsId = i.getShopsId();
            i.getProductDtoList().forEach(product -> {
                inputList.add(ShopProductIdInput.builder().shopId(shopsId).productId(product.getProductId()).build());
            });
        });
        return inwardServletManager.getProductDetails(inputList);
    }


    /**
     * 判断预定人账户金额是否足够
     *
     * @param totalAmount 订单总金额
     * @param userAccount 用户账户余额信息
     * @throws OrderException
     */
    private void checkoutOrderAccount(BigDecimal totalAmount, UserAccountOutput userAccount) throws OrderException {
        BigDecimal mainAmount = Optional.of(userAccount).map(UserAccountOutput::getMainAmount).orElse(BigDecimal.ZERO);
        BigDecimal giveAmount = Optional.of(userAccount).map(UserAccountOutput::getGiveAmount).orElse(BigDecimal.ZERO);
        BigDecimal frozenAmount = Optional.of(userAccount).map(UserAccountOutput::getFrozenAmount).orElse(BigDecimal.ZERO);
        BigDecimal userAmount = mainAmount.add(giveAmount).subtract(frozenAmount);
        if (userAmount.compareTo(totalAmount) < OrderConstant.ZERO) {
            String userId = userAccount.getUserId();
            log.warn("[order]-create userAmount insufficient balance！userId:{}, userAmount:{}, totalAmount:{}", userId, userAmount, totalAmount);
            throw new OrderException(OrderCode.INSUFFICIENT_BALANCE);
        }
    }

}
