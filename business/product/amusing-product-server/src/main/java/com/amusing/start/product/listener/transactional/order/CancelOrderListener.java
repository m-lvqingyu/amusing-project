package com.amusing.start.product.listener.transactional.order;

import com.amusing.start.client.api.OrderClient;
import com.amusing.start.product.enums.MessageStatus;
import com.amusing.start.product.pojo.ReduceStockMsgInfo;
import com.amusing.start.product.service.ReduceStockMsgInfoService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

/**
 * @author lvQingYu
 * @version 1.0
 * @description: 取消订单事务监听
 * @date 2021/11/4 24:32
 */
@Slf4j
@Component
@RocketMQTransactionListener(rocketMQTemplateBeanName = "cancelOrderMqTemplate")
public class CancelOrderListener implements RocketMQLocalTransactionListener {

    private static final String MESSAGE_ID_KEY = "msgUid";

    private final OrderClient orderClient;

    private final ReduceStockMsgInfoService reduceStockMsgInfoService;


    @Autowired
    public CancelOrderListener(OrderClient orderClient, ReduceStockMsgInfoService reduceStockMsgInfoService) {
        this.orderClient = orderClient;
        this.reduceStockMsgInfoService = reduceStockMsgInfoService;
    }

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        ReduceStockMsgInfo msgInfo = analysisBody(message);
        if (msgInfo == null) {
            log.warn("[Product]-[CancelOrderListener]-ReduceStockMsgInfo is null");
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        Integer status = msgInfo.getStatus();
        if (status != MessageStatus.INIT.getCode()) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        Integer result = updateMsgStatus(msgInfo.getMsgId());
        if (result == null || result <= 0) {
            return RocketMQLocalTransactionState.UNKNOWN;
        }
        return RocketMQLocalTransactionState.COMMIT;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        ReduceStockMsgInfo msgInfo = analysisBody(message);
        if (msgInfo == null) {
            log.warn("[Product]-[CancelOrderListener]-ReduceStockMsgInfo is null");
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        Integer status = msgInfo.getStatus();
        if (status != null && status == MessageStatus.DEDUCTION_FAILED.getCode()) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        String msgId = msgInfo.getMsgId();
        String orderNo = msgInfo.getOrderNo();
        Boolean isCancel = null;
        try {
            isCancel = orderClient.isCancel(orderNo);
        } catch (Exception e) {
            log.error("Product]-[CancelOrderListener]-getOrderIsCancel err! msgId:{}, orderNo:{}, msg:{}",
                    msgId,
                    orderNo,
                    Throwables.getStackTraceAsString(e));
        }
        if (isCancel == null) {
            return RocketMQLocalTransactionState.UNKNOWN;
        }
        if (isCancel) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        Integer result = updateMsgStatus(msgInfo.getMsgId());
        if (result == null || result <= 0) {
            return RocketMQLocalTransactionState.UNKNOWN;
        }
        return RocketMQLocalTransactionState.COMMIT;
    }

    /**
     * 取消订单消息体解析
     *
     * @param message
     * @return
     */
    private ReduceStockMsgInfo analysisBody(Message message) {
        MessageHeaders headers = message.getHeaders();
        if (headers == null) {
            log.warn("[Product]-[CancelOrderListener]-message header is null");
            return null;
        }

        String msgId = headers.get(MESSAGE_ID_KEY, String.class);
        if (StringUtils.isEmpty(msgId)) {
            log.warn("[Product]-[CancelOrderListener]-messageId is null");
            return null;
        }

        ReduceStockMsgInfo msgInfo = null;
        try {
            msgInfo = reduceStockMsgInfoService.getMsgInfo(msgId);
        } catch (Exception e) {
            log.error("[Product]-[CancelOrderListener]-getReduceStockMsgInfo err! msgId:{}, msg:{}",
                    msgId,
                    Throwables.getStackTraceAsString(e));
        }
        if (msgInfo == null) {
            log.warn("[Product]-[CancelOrderListener]-ReduceStockMsgInfo is null! msgId:{}", msgId);
            return null;
        }
        log.info("[Product]-[CancelOrderListener]-msgId:{}, msgInfo:{}", msgId, msgInfo);
        return msgInfo;
    }

    /**
     * 更新扣减库存消息处理状态
     *
     * @param msgId
     * @return
     */
    private Integer updateMsgStatus(String msgId) {
        Integer result = null;
        try {
            result = reduceStockMsgInfoService.updateMsgStatus(msgId, MessageStatus.DEDUCTION_FAILED.getCode());
        } catch (Exception e) {
            log.error("[Product]-[CancelOrderListener]-updateReduceStockMsgInfo err! msgId:{}, msg:{}",
                    msgId,
                    Throwables.getStackTraceAsString(e));
        }
        return result;
    }

}
