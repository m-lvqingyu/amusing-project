package com.amusing.start.platform.service.pay.ali.impl;

import com.amusing.start.client.api.OrderClient;
import com.amusing.start.client.input.AliPayAsyncNotifyInput;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.platform.constant.AliPayConstant;
import com.amusing.start.platform.constant.CacheKey;
import com.amusing.start.platform.entity.pojo.AliPayAsyncNotifyInfo;
import com.amusing.start.platform.enums.AliPayAsyncNotifyStatus;
import com.amusing.start.platform.enums.AliTradeOrderStatus;
import com.amusing.start.platform.mapper.AliPayAsyncNotifyInfoMapper;
import com.amusing.start.platform.service.pay.ali.PayNotifyService;
import com.amusing.start.result.ApiResult;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/10/30
 */
@Slf4j
@Service
public class PayNotifyServiceImpl implements PayNotifyService {

    @Resource
    private AliPayAsyncNotifyInfoMapper aliPayAsyncNotifyInfoMapper;

    private final OrderClient orderClient;

    private final RedissonClient redissonClient;

    @Autowired
    public PayNotifyServiceImpl(OrderClient orderClient,
                                RedissonClient redissonClient) {
        this.orderClient = orderClient;
        this.redissonClient = redissonClient;
    }

    @Override
    public String pcAsyncNotify(String notifyId,
                                String buyerLogonId,
                                String orderNo,
                                String tradeNo,
                                String tradeStatus,
                                String totalAmount,
                                String receiptAmount,
                                String invoiceAmount,
                                String buyerPayAmount,
                                String pointAmount,
                                String gmtPayment) {
        if (!AliTradeOrderStatus.TRADE_SUCCESS.name().equalsIgnoreCase(tradeStatus)) {
            return AliPayConstant.SUCCESS;
        }
        String lockKey = CacheKey.getOrderNotifyLockKey(notifyId);
        RLock lock = redissonClient.getLock(lockKey);
        if (!lock.tryLock()) {
            log.warn("[PcAsyncNotify]-fail! not obtained lock! notifyId:{}", notifyId);
            return AliPayConstant.FAIL;
        }
        AliPayAsyncNotifyInfo notifyInfo = null;
        try {
            notifyInfo = aliPayAsyncNotifyInfoMapper.getByNotifyId(notifyId);
        } catch (Exception e) {
            log.warn("[PcAsyncNotify]-err! notifyId:{}, msg:{}", notifyId, Throwables.getStackTraceAsString(e));
        }
        if (notifyInfo != null) {
            lock.unlock();
            return AliPayConstant.SUCCESS;
        }
        notifyInfo = buildNotifyInfo(notifyId, orderNo, tradeNo, tradeStatus);
        Integer insert = null;
        try {
            insert = aliPayAsyncNotifyInfoMapper.insert(notifyInfo);
        } catch (Exception e) {
            log.warn("[PcAsyncNotify]-err! notifyId:{}, msg:{}", notifyId, Throwables.getStackTraceAsString(e));
        }
        if (insert == null || insert <= CommConstant.ZERO) {
            lock.unlock();
            return AliPayConstant.FAIL;
        }
        AliPayAsyncNotifyInput input = new AliPayAsyncNotifyInput()
                .setNotifyId(notifyId)
                .setOrderNo(orderNo)
                .setTradeNo(tradeNo)
                .setBuyerLogonId(buyerLogonId)
                .setTotalAmount(amountConvert(totalAmount))
                .setReceiptAmount(amountConvert(receiptAmount))
                .setBuyerPayAmount(amountConvert(buyerPayAmount))
                .setPointAmount(amountConvert(pointAmount))
                .setInvoiceAmount(amountConvert(invoiceAmount))
                .setMerchantDiscountAmount(CommConstant.ZERO)
                .setDiscountAmount(CommConstant.ZERO)
                .setGmtPayment(gmtPayment);
        ApiResult<Boolean> apiResult = orderClient.aliPayNotifySuccess(input);
        if (apiResult != null && apiResult.isSuccess()) {
            updateAsyncNotifyStatus(notifyInfo.getId(), AliPayAsyncNotifyStatus.FINISH.getKey());
        }
        return AliPayConstant.SUCCESS;
    }

    @Override
    public Boolean updateAsyncNotifyStatus(Long id, Integer status) {
        return aliPayAsyncNotifyInfoMapper.updateById(id, status) > CommConstant.ZERO;
    }

    private AliPayAsyncNotifyInfo buildNotifyInfo(String notifyId,
                                                  String orderNo,
                                                  String tradeNo,
                                                  String tradeStatus) {
        long time = System.currentTimeMillis();
        return new AliPayAsyncNotifyInfo()
                .setNotifyId(notifyId)
                .setOrderNo(orderNo)
                .setTradeNo(tradeNo)
                .setTradeStatus(tradeStatus)
                .setStatus(AliPayAsyncNotifyStatus.INIT.getKey())
                .setCreateTime(time)
                .setUpdateTime(time);
    }

    private Integer amountConvert(String amount) {
        if (StringUtils.isBlank(amount)) {
            return CommConstant.ZERO;
        }
        return new BigDecimal(amount).multiply(new BigDecimal(CommConstant.ONE_HUNDRED)).intValue();
    }

}
