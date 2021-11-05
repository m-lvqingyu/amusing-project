package com.amusing.start.product.listener.transactional.account;

import com.amusing.start.product.enums.MessageStatus;
import com.amusing.start.product.listener.producer.ProductProducer;
import com.amusing.start.product.pojo.ReduceStockMsgInfo;
import com.amusing.start.product.service.ProductService;
import com.amusing.start.product.service.ReduceStockMsgInfoService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2021/11/4 22:33
 */
@Slf4j
@Component
@RocketMQTransactionListener(rocketMQTemplateBeanName = "accountDeductionMqTemplate")
public class AccountDeductionListener implements RocketMQLocalTransactionListener {

    private static final String MESSAGE_ID_KEY = "msgUid";

    private final ProductService productService;

    private final ProductProducer productProducer;

    private final ReduceStockMsgInfoService reduceStockMsgInfoService;

    public AccountDeductionListener(ProductService productService, ProductProducer productProducer, ReduceStockMsgInfoService reduceStockMsgInfoService) {
        this.productService = productService;
        this.productProducer = productProducer;
        this.reduceStockMsgInfoService = reduceStockMsgInfoService;
    }

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        ReduceStockMsgInfo msgInfo = getReduceStockMsgInfo(message);
        if (msgInfo == null) {
            log.warn("[Product]-[AccountDeductionListener]-msgInfo is null!");
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        log.info("[Product]-[AccountDeductionListener]-msgInfo:{}", msgInfo);

        Integer status = msgInfo.getStatus();
        if (status == null || status != MessageStatus.INIT.getCode()) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        String msgId = msgInfo.getMsgId();
        boolean result = false;
        try {
            result = deductionProductStock(msgId, msgInfo);
        } catch (Exception e) {
            log.error("[Product]-[AccountDeductionListener]-deductionProductStock err! msgId:{}, msg:{}",
                    msgId,
                    Throwables.getStackTraceAsString(e));
        }
        if (result) {
            return RocketMQLocalTransactionState.COMMIT;
        }

        // 发送取消订单消息
        String orderNo = msgInfo.getOrderNo();
        try {
            productProducer.sendCancelOrderMsg(msgId, orderNo);
        } catch (Exception e) {
            log.error("[Product]-[AccountDeductionListener]-sendCancelOrderMsg err! msgId:{}, msg:{}",
                    msgId,
                    Throwables.getStackTraceAsString(e));
            return RocketMQLocalTransactionState.UNKNOWN;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        ReduceStockMsgInfo msgInfo = getReduceStockMsgInfo(message);
        if (msgInfo == null) {
            log.warn("[Product]-[AccountDeductionListener]-checkLocalTransaction msgInfo is null!");
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        log.info("[Product]-[AccountDeductionListener]-checkLocalTransaction msgInfo:{}", msgInfo);

        Integer status = msgInfo.getStatus();
        if (status == null || status == MessageStatus.INIT.getCode()) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        return RocketMQLocalTransactionState.COMMIT;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deductionProductStock(String msgId, ReduceStockMsgInfo reduceStockMsgInfo) {
        String orderNo = reduceStockMsgInfo.getOrderNo();
        String shopsId = reduceStockMsgInfo.getShopsId();
        String productId = reduceStockMsgInfo.getProductId();
        Integer productNum = reduceStockMsgInfo.getProductNum();
        boolean deductionStockResult = productService.deductionProductStock(shopsId, productId, productNum);
        log.info("[Product]-[ReduceStockConsumer]-orderNo:{}, productId:{} deductionStockResult:{}", orderNo, productId, deductionStockResult);
        if (deductionStockResult) {
            reduceStockMsgInfoService.updateMsgStatus(msgId, MessageStatus.DEDUCTION_SUCCESS.getCode());
        }
        return deductionStockResult;
    }

    /**
     * 获取扣减库存消息内容
     *
     * @param message
     * @return
     */
    private ReduceStockMsgInfo getReduceStockMsgInfo(Message message) {
        MessageHeaders headers = message.getHeaders();
        if (headers == null) {
            log.warn("[Product]-[AccountDeductionListener]-message header is null");
            return null;
        }

        String msgId = headers.get(MESSAGE_ID_KEY, String.class);
        if (StringUtils.isEmpty(msgId)) {
            log.warn("[Product]-[AccountDeductionListener]-messageId is null");
            return null;
        }

        ReduceStockMsgInfo msgInfo = null;
        try {
            msgInfo = reduceStockMsgInfoService.getMsgInfo(msgId);
        } catch (Exception e) {
            log.error("[Product]-[AccountDeductionListener]-getMsgInfo err! msgId:{}, msg:{}",
                    msgId,
                    Throwables.getStackTraceAsString(e));
        }
        return msgInfo;
    }


}
