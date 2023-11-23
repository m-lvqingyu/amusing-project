package com.amusing.start.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.input.PayInput;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ShopCarOutput;
import com.amusing.start.code.BaseCode;
import com.amusing.start.code.CommCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.exception.InnerApiException;
import com.amusing.start.order.constant.CacheKey;
import com.amusing.start.order.convert.OrderTransformationUtils;
import com.amusing.start.order.entity.dto.CreateDto;
import com.amusing.start.order.entity.pojo.OrderAliPayInfo;
import com.amusing.start.order.entity.pojo.OrderProductInfo;
import com.amusing.start.order.entity.pojo.OrderShopsInfo;
import com.amusing.start.order.entity.vo.OrderDetailVo;
import com.amusing.start.order.entity.vo.OrderProductVo;
import com.amusing.start.order.entity.vo.OrderShopsVo;
import com.amusing.start.order.enums.*;
import com.amusing.start.order.enums.code.OrderErrorCode;
import com.amusing.start.order.mapper.OrderAliPayInfoMapper;
import com.amusing.start.order.mapper.OrderInfoMapper;
import com.amusing.start.order.entity.pojo.OrderInfo;
import com.amusing.start.order.mapper.OrderProductInfoMapper;
import com.amusing.start.order.mapper.OrderShopsInfoMapper;
import com.amusing.start.order.service.OrderService;
import com.amusing.start.result.ApiResult;
import com.google.common.base.Throwables;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Lv.QingYu
 * @description: 订单ServiceImpl
 * @since 2021/10/10
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private OrderShopsInfoMapper orderShopsInfoMapper;
    @Resource
    private OrderProductInfoMapper orderProductInfoMapper;
    @Resource
    private OrderAliPayInfoMapper orderAliPayInfoMapper;

    private final ProductClient productClient;
    private final UserClient userClient;
    private final RedissonClient redissonClient;
    private final OrderTransformationUtils orderTransformationUtils;

    @Autowired
    public OrderServiceImpl(ProductClient productClient,
                            UserClient userClient,
                            RedissonClient redissonClient,
                            OrderTransformationUtils orderTransformationUtils) {
        this.productClient = productClient;
        this.userClient = userClient;
        this.redissonClient = redissonClient;
        this.orderTransformationUtils = orderTransformationUtils;
    }

    @Value("${order.worker}")
    private Long orderWorker;

    @Value("${order.dataCenter}")
    private Long orderDataCenter;

    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String create(String userId, CreateDto createDto) {
        String createLockKey = CacheKey.orderCreateLock(userId);
        RLock lock = redissonClient.getLock(createLockKey);
        if (!lock.tryLock()) {
            throw new CustomException(CommCode.REQUEST_LIMIT_ERR);
        }
        try {
            // 获取用户账户余额
            ApiResult<Integer> apiResult = userClient.accountBalance(userId);
            if (!apiResult.isSuccess()) {
                throw new CustomException(OrderErrorCode.ACCOUNT_NOT_FOUND);
            }
            // 获取用户购物车详情
            List<ShopCarOutput> shopCarList = productClient.shopCar(userId);
            shopCarList = Optional.ofNullable(shopCarList).filter(CollectionUtil::isNotEmpty).orElseThrow(
                    () -> new CustomException(OrderErrorCode.SHOP_CAR_ERROR)
            );
            // 生成订单编号
            String orderNo = IdUtil.getSnowflake(orderWorker, orderDataCenter).nextIdStr();
            // 订单-商铺关系集合
            List<OrderShopsInfo> shopsInfoList = new ArrayList<>();
            // 订单-商品关系集合
            List<OrderProductInfo> productInfoList = new ArrayList<>();
            // 商品-数量关系集合
            List<StockDeductionInput> deductionInputList = new ArrayList<>();
            // 遍历购物车，构建订单-商铺、商品预计商品-数量关系集合
            shopCarList.forEach(shop -> {
                shopsInfoList.add(orderTransformationUtils.beanTransformation(shop, orderNo));
                shop.getProductOutputList().forEach(product -> {
                    int amount = product.getPrice() * product.getStock();
                    productInfoList.add(orderTransformationUtils.beanTransformation(
                                    orderNo,
                                    amount,
                                    shop.getShopId(),
                                    product
                            )
                    );
                    deductionInputList.add(orderTransformationUtils.beanTransformation(product));
                });
            });
            // 校验订单金额是否超过用户可用余额
            Integer userAmount = apiResult.getData();
            int totalAmount = productInfoList.stream().mapToInt(OrderProductInfo::getAmount).sum();
            if (userAmount == null || userAmount < totalAmount) {
                throw new CustomException(OrderErrorCode.ACCOUNT_INSUFFICIENT);
            }
            // 构建订单信息
            OrderInfo orderInfo = orderTransformationUtils.buildDefOrder();
            orderInfo.setOrderNo(orderNo);
            orderInfo.setReserveId(userId);
            orderInfo.setConsigneeId(createDto.getConsigneeId());
            orderInfo.setTotalAmount(totalAmount);
            orderInfo.setRealAmount(totalAmount);
            orderInfo.setCreateBy(userId);
            orderInfo.setUpdateBy(userId);
            // 保存订单信息
            checkResult(orderInfoMapper.insert(orderInfo));
            // 保存订单-商品关系信息
            productInfoList.forEach(i -> {
                checkResult(orderProductInfoMapper.insert(i));
            });
            // 保存订单-商铺关系信息
            shopsInfoList.forEach(i -> {
                checkResult(orderShopsInfoMapper.insert(i));
            });
            // 扣减账户余额
            checkResult(
                    userClient.payment(new PayInput().setUserId(userId).setAmount(totalAmount))
            );
            // 扣减商品库存
            checkResult(productClient.deductionStock(deductionInputList));
            return orderNo;
        } catch (InnerApiException | CustomException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e;
        } catch (Exception e) {
            log.error("[OrderCreate]-err! msg:{}", Throwables.getStackTraceAsString(e));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new CustomException(CommCode.SERVICE_ERR);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public OrderDetailVo getOrderDetail(String userId, String orderNo) {
        OrderInfo orderInfo = Optional.ofNullable(orderInfoMapper.getByNo(userId, orderNo))
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));
        List<OrderShopsInfo> shopsInfoList = Optional.ofNullable(orderShopsInfoMapper.getByNo(orderNo))
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));
        List<OrderProductInfo> productInfoList = Optional.ofNullable(orderProductInfoMapper.getByNo(orderNo))
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));
        List<OrderShopsVo> shopsVoList = new ArrayList<>();
        shopsInfoList.forEach(i -> {
            OrderShopsVo orderShopsVo = new OrderShopsVo()
                    .setShopsId(i.getShopsId())
                    .setShopsName(i.getShopsName())
                    .setSort(i.getSort());
            String shopsId = i.getShopsId();
            List<OrderProductVo> orderProductVoList = new ArrayList<>();
            productInfoList.forEach(j -> {
                if (shopsId.equals(j.getShopId())) {
                    orderProductVoList.add(BeanUtil.copyProperties(j, OrderProductVo.class));
                }
            });
            orderShopsVo.setProductVoList(orderProductVoList);
            shopsVoList.add(orderShopsVo);
        });
        return new OrderDetailVo().setOrderNo(orderNo)
                .setReserveId(orderInfo.getReserveId())
                .setConsigneeId(orderInfo.getConsigneeId())
                .setTotalAmount(orderInfo.getTotalAmount())
                .setFreightAmount(orderInfo.getFreightAmount())
                .setCouponAmount(orderInfo.getCouponAmount())
                .setActivityAmount(orderInfo.getActivityAmount())
                .setStatus(orderInfo.getStatus())
                .setIsFreight(orderInfo.getIsFreight())
                .setIsEvaluate(orderInfo.getIsEvaluate())
                .setShopsVoList(shopsVoList);
    }

    @Override
    public OrderInfo getByNo(String orderNo) {
        OrderInfo orderInfo = orderInfoMapper.getByNo(null, orderNo);
        return Optional.of(orderInfo).orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    public Boolean aliPaySuccess(Integer type,
                                 String notifyId,
                                 String orderNo,
                                 String tradeNo,
                                 String buyerLogonId,
                                 Integer totalAmount,
                                 Integer receiptAmount,
                                 Integer buyerPayAmount,
                                 Integer pointAmount,
                                 Integer invoiceAmount,
                                 Integer merchantDiscountAmount,
                                 Integer discountAmount,
                                 String gmtPayment) {
        String lockKey = CacheKey.orderStatusChangeLock(orderNo);
        RLock lock = redissonClient.getLock(lockKey);
        if (!lock.tryLock()) {
            log.warn("[AliPaySuccess]-get change order status lock fail! orderNo:{}", orderNo);
            return Boolean.FALSE;
        }
        OrderInfo orderInfo = null;
        try {
            orderInfo = getByNo(orderNo);
        } catch (Exception e) {
            log.error("[AliPaySuccess]-not found orderInfo! orderNo:{}, msg:{}",
                    orderNo,
                    Throwables.getStackTraceAsString(e));
        }
        if (orderInfo == null) {
            lock.unlock();
            return Boolean.FALSE;
        }
        if (OrderStatusEnum.SCHEDULED.getKey() != orderInfo.getStatus()) {
            log.warn("[AliPaySuccess]-order status err! orderNo:{}, status:{}", orderNo, orderInfo.getStatus());
            lock.unlock();
            return Boolean.FALSE;
        }
        long time = System.currentTimeMillis();
        OrderAliPayInfo orderAliPayInfo = new OrderAliPayInfo()
                .setNotifyId(notifyId)
                .setOrderNo(orderNo)
                .setTradeNo(tradeNo)
                .setBuyerLogonId(buyerLogonId)
                .setType(type)
                .setTotalAmount(totalAmount)
                .setReceiptAmount(receiptAmount)
                .setBuyerPayAmount(buyerPayAmount)
                .setPointAmount(pointAmount)
                .setInvoiceAmount(invoiceAmount)
                .setMerchantDiscountAmount(merchantDiscountAmount)
                .setDiscountAmount(discountAmount)
                .setGmtPayment(gmtPayment)
                .setStatus(OrderPayStatusEnum.SUCCESS.getKey())
                .setCreateTime(time)
                .setUpdateTime(time);
        try {
            Integer insert = orderAliPayInfoMapper.insert(orderAliPayInfo);
            if (insert == null || insert <= CommConstant.ZERO) {
                throw new CustomException(CommCode.SERVICE_ERR);
            }
            insert = orderInfoMapper.updateStatus(orderNo, OrderStatusEnum.PAY.getKey(), orderAliPayInfo.getId());
            if (insert == null || insert <= CommConstant.ZERO) {
                throw new CustomException(CommCode.SERVICE_ERR);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("[AliPaySuccess]-err! orderNo:{}, msg:{}", orderNo, Throwables.getStackTraceAsString(e));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Boolean.FALSE;
        } finally {
            lock.unlock();
        }
    }

    private void checkResult(Integer result) {
        if (result == null || result <= CommConstant.ZERO) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
    }

    private void checkResult(ApiResult<Boolean> result) {
        if (!result.isSuccess()) {
            log.warn("[ApiClient]-result:{}", JSONObject.toJSONString(result));
            throw new InnerApiException(result.getCode(), result.getMessage());
        }
    }

}
