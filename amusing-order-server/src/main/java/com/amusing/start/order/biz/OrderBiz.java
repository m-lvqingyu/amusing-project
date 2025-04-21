package com.amusing.start.order.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.amusing.start.client.api.UserFeignClient;
import com.amusing.start.client.request.AccountPayRequest;
import com.amusing.start.client.request.ConsigneeInfoRequest;
import com.amusing.start.client.request.StockDeductionRequest;
import com.amusing.start.client.response.ConsigneeResp;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.exception.InnerApiException;
import com.amusing.start.order.enums.OrderCanRefund;
import com.amusing.start.order.enums.OrderErrorCode;
import com.amusing.start.order.enums.OrderPayType;
import com.amusing.start.order.enums.OrderStatus;
import com.amusing.start.order.mapper.OrderProductMapper;
import com.amusing.start.order.mapper.OrderShopsMapper;
import com.amusing.start.order.pojo.Order;
import com.amusing.start.order.pojo.OrderProduct;
import com.amusing.start.order.pojo.OrderShops;
import com.amusing.start.order.req.ApiCreateOrderReq;
import com.amusing.start.order.resp.ApiShopCarDetailResp;
import com.amusing.start.order.service.OrderService;
import com.amusing.start.order.service.ProductService;
import com.amusing.start.order.utils.OrderCacheUtil;
import com.amusing.start.result.ApiResult;
import com.google.common.base.Throwables;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2025/4/8
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OrderBiz {

    private final RedissonClient redissonClient;

    private final UserFeignClient userFeignClient;

    private final OrderService orderService;

    private final ProductService productService;

    private final OrderShopsMapper orderShopsMapper;

    private final OrderProductMapper orderProductMapper;

    private final ShopCarBiz shopCarBiz;

    @Value("${order.worker}")
    private Long worker;

    @Value("${order.dataCenter}")
    private Long dataCenter;

    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public String create(String userId, ApiCreateOrderReq dto) {
        RLock lock = redissonClient.getLock(OrderCacheUtil.createLock(userId));
        if (!lock.tryLock()) {
            throw new CustomException(CommunalCode.REQUEST_LIMIT_ERR);
        }
        try {
            // 收货地址信息
            ApiResult<ConsigneeResp> consigneeResult = userFeignClient.consigneeInfo(
                    new ConsigneeInfoRequest()
                            .setAddressId(dto.getId())
                            .setUserId(userId));
            if (consigneeResult.isSuccess()) {
                throw new CustomException(CommunalCode.PARAMETER_ERR);
            }
            ConsigneeResp consigneeResp = consigneeResult.getData();

            // 下单人账户余额
            ApiResult<Integer> accountResult = userFeignClient.balance(userId);
            if (accountResult.isSuccess()) {
                throw new CustomException(OrderErrorCode.ACCOUNT_NOT_FOUND);
            }
            Integer amount = accountResult.getData();

            String orderNo = IdUtil.getSnowflake(worker, dataCenter).nextIdStr();
            List<OrderShops> shopsInfoList = new ArrayList<>();
            List<OrderProduct> productInfoList = new ArrayList<>();
            List<StockDeductionRequest> deductionInputList = new ArrayList<>();
            buildOrderRelational(userId, orderNo, shopsInfoList, productInfoList, deductionInputList);

            // 校验订单金额是否超过用户可用余额
            int totalAmount = productInfoList.stream().mapToInt(OrderProduct::getAmount).sum();
            if (amount < totalAmount) {
                throw new CustomException(OrderErrorCode.ACCOUNT_INSUFFICIENT);
            }
            // 保存订单
            Order order = buildOrder(orderNo, userId, totalAmount, consigneeResp);
            if (orderService.insert(order) <= CommConstant.ZERO) {
                throw new CustomException(CommunalCode.SERVICE_ERR);
            }
            // 保存订单-商铺关联关系
            shopsInfoList.forEach(i -> {
                if (orderShopsMapper.insert(i) <= CommConstant.ZERO) {
                    throw new CustomException(CommunalCode.SERVICE_ERR);
                }
            });
            // 保存订单-商品关联关系
            productInfoList.forEach(i -> {
                if (orderProductMapper.insert(i) <= CommConstant.ZERO) {
                    throw new CustomException(CommunalCode.SERVICE_ERR);
                }
            });
            // 扣减库存
            productService.deductionStock(deductionInputList);
            // 扣减余额
            ApiResult<Boolean> apiResult = userFeignClient.payment(new AccountPayRequest()
                    .setUserId(userId)
                    .setAmount(totalAmount)
                    .setOrigAmount(amount));
            if (apiResult.isSuccess() && !apiResult.getData()) {
                throw new InnerApiException(apiResult.getCode(), apiResult.getMessage());
            }
            return orderNo;
        } catch (InnerApiException | CustomException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e;
        } catch (Exception e) {
            log.error("[OrderCreate]-err! msg:{}", Throwables.getStackTraceAsString(e));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new CustomException(CommunalCode.SERVICE_ERR);
        } finally {
            lock.unlock();
        }
    }
    
    private void buildOrderRelational(String userId,
                                      String orderNo,
                                      List<OrderShops> shopsInfoList,
                                      List<OrderProduct> productInfoList,
                                      List<StockDeductionRequest> deductionInputList) {
        // 购物车信息
        List<ApiShopCarDetailResp> shopCarList = shopCarBiz.shopCar(userId);
        if (CollectionUtil.isEmpty(shopCarList)) {
            throw new CustomException(OrderErrorCode.SHOP_CAR_ERROR);
        }
        shopCarList.forEach(shop -> {
            shopsInfoList.add(new OrderShops()
                    .setShopsId(shop.getShopId())
                    .setShopsName(shop.getShopName())
                    .setOrderNo(orderNo)
                    .setSort(shop.getSort()));
            shop.getProductDetailList().forEach(product -> {
                int amount = product.getPrice() * product.getStock();
                productInfoList.add(new OrderProduct()
                        .setOrderNo(orderNo)
                        .setShopId(shop.getShopId())
                        .setProductId(product.getProductId())
                        .setProductName(product.getProductName())
                        .setPriceId(product.getPriceId())
                        .setPrice(product.getPrice())
                        .setNum(product.getStock())
                        .setAmount(amount));
                deductionInputList.add(new StockDeductionRequest()
                        .setId(product.getProductId())
                        .setNum(product.getStock()));
            });
        });
    }

    public Order buildOrder(String orderNo, String userId, Integer totalAmount, ConsigneeResp consigneeInfo) {
        Long currentTime = System.currentTimeMillis();
        return new Order().setOrderNo(orderNo)
                .setUserId(userId)
                .setName(consigneeInfo.getName())
                .setPhone(consigneeInfo.getPhone())
                .setProvinces(consigneeInfo.getProvinces())
                .setCities(consigneeInfo.getCities())
                .setDistricts(consigneeInfo.getDistricts())
                .setAddress(consigneeInfo.getAddress())
                .setTotalAmount(totalAmount)
                .setRealAmount(totalAmount)
                .setReductionAmount(CommConstant.ZERO)
                .setStatus(OrderStatus.SCHEDULED.getKey())
                .setPayType(OrderPayType.UNKNOWN.getKey())
                .setPayId(CommConstant.ZERO_LONG)
                .setCanRefund(OrderCanRefund.YES.getKey())
                .setRefundAmount(CommConstant.ZERO)
                .setCreateTime(currentTime)
                .setCreateBy(userId)
                .setUpdateTime(currentTime)
                .setUpdateBy(userId);
    }

}
