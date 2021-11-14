package com.amusing.start.order.listener.transactional.product;

import com.alibaba.fastjson.JSONObject;
import com.amusing.start.order.enums.MsgStatus;
import com.amusing.start.order.listener.message.OrderCreateMsg;
import com.amusing.start.order.pojo.OrderMessageInfo;
import com.amusing.start.order.service.IOrderMessageInfoService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * Create By 2021/11/13
 *
 * @author lvqingyu
 */
@Slf4j
@RocketMQTransactionListener(rocketMQTemplateBeanName = "createOrderMqTemplate")
public class CreateOrderTransactionListener implements RocketMQLocalTransactionListener {

    private final IOrderMessageInfoService createOrderMsgService;

    @Autowired
    public CreateOrderTransactionListener(IOrderMessageInfoService createOrderMsgService) {
        this.createOrderMsgService = createOrderMsgService;
    }

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object arg) {
        String transactionId = getTransactionId(message);
        if (StringUtils.isEmpty(transactionId)) {
            log.warn("[Order]-[create]-executeLocal transactionId is null");
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        OrderCreateMsg orderCreateMsg = getOrderCreateMsg(transactionId, message);
        if (orderCreateMsg == null) {
            log.warn("[Order]-[create]-executeLocal orderCreateMsg is null, transactionId:{}", transactionId);
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        String orderNo = orderCreateMsg.getOrderNo();
        OrderMessageInfo createOrderMsg = getCreateOrderMsg(orderNo, transactionId);
        if (createOrderMsg == null) {
            log.warn("[Order]-[create]-executeLocal createOrderMsg null found, transactionId:{}, orderNo:{}", transactionId, orderNo);
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        Integer msgStatus = createOrderMsg.getMsgStatus();
        boolean flag = msgStatus != null && (MsgStatus.SEND.getKey() == msgStatus || MsgStatus.SEND_FAIL.getKey() == msgStatus);
        if (flag) {
            log.warn("[Order]-[create]-executeLocal msg is send, transactionId:{}, orderNo:{}", transactionId, orderNo);
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        try {
            createOrderMsgService.updateMsgStatus(orderNo, MsgStatus.SEND.getKey());
        } catch (Exception e) {
            log.error("[Order]-[create]-executeLocal updateMsgStatus err, transactionId:{}, orderNo:{}, msg:{}",
                    transactionId,
                    orderNo,
                    Throwables.getStackTraceAsString(e));
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        return RocketMQLocalTransactionState.COMMIT;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        String transactionId = getTransactionId(message);
        if (StringUtils.isEmpty(transactionId)) {
            log.warn("[Order]-[create]-checkLocal transactionId is null");
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        OrderCreateMsg orderCreateMsg = getOrderCreateMsg(transactionId, message);
        if (orderCreateMsg == null) {
            log.warn("[Order]-[create]-checkLocal orderCreateMsg is null, transactionId:{}", transactionId);
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        String orderNo = orderCreateMsg.getOrderNo();
        OrderMessageInfo createOrderMsg = getCreateOrderMsg(orderNo, transactionId);
        if (createOrderMsg == null) {
            log.warn("[Order]-[create]-checkLocal createOrderMsg null found, transactionId:{}, orderNo:{}", transactionId, orderNo);
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        Integer msgStatus = createOrderMsg.getMsgStatus();
        if (msgStatus != null && msgStatus == MsgStatus.SEND.getKey()) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }

    /**
     * 获得事务ID
     *
     * @param message
     * @return
     */
    private String getTransactionId(Message message) {
        MessageHeaders headers = message.getHeaders();
        if (headers == null) {
            log.warn("[Order]-[create]-CreateOrderTransactionListener message headers is null");
            return null;
        }
        String transactionId = headers.get(RocketMQHeaders.TRANSACTION_ID, String.class);
        return transactionId;
    }

    private OrderCreateMsg getOrderCreateMsg(String transactionId, Message message) {
        Object payload = message.getPayload();
        String body = new String((byte[]) payload);
        if (StringUtils.isEmpty(body)) {
            log.warn("[Order]-[create]-CreateOrderTransactionListener body is null, transactionId:{}", transactionId);
            return null;
        }
        OrderCreateMsg orderCreateMsg = null;
        try {
            orderCreateMsg = JSONObject.parseObject(body, OrderCreateMsg.class);
        } catch (Exception e) {
            log.error("[Order]-[create]-CreateOrderTransactionListener analysis err, transactionId:{}, msg:{}",
                    transactionId,
                    Throwables.getStackTraceAsString(e));
        }
        return orderCreateMsg;
    }

    private OrderMessageInfo getCreateOrderMsg(String orderNo, String transactionId) {
        OrderMessageInfo createOrderMsg = null;
        try {
            createOrderMsg = createOrderMsgService.getCreateOrderMsgByOrderNo(orderNo);
        } catch (Exception e) {
            log.error("[Order]-[create]-CreateOrderTransactionListener getCreateOrderMsg err, transactionId:{}, orderNo:{}, msg:{}",
                    transactionId,
                    orderNo,
                    Throwables.getStackTraceAsString(e));
        }
        return createOrderMsg;
    }

}
