package com.amusing.start.product.listener.producer;

import com.alibaba.fastjson.JSONObject;
import com.amusing.start.product.listener.message.AccountDeductionMsg;
import com.amusing.start.product.listener.transactional.account.AccountDeductionMqTemplate;
import com.amusing.start.product.listener.transactional.order.CancelOrderMqTemplate;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2021/11/5 22:03
 */
@Slf4j
@Component
public class ProductProducer {

    private static final String MESSAGE_ID_KEY = "msgUid";

    private static final String CANCEL_ORDER_TOPIC = "cancel_order";

    private static final String ACCOUNT_DEDUCTION_TOPIC = "account_deduction";

    private final CancelOrderMqTemplate cancelOrderMqTemplate;

    private final AccountDeductionMqTemplate accountDeductionMqTemplate;

    @Autowired
    public ProductProducer(CancelOrderMqTemplate cancelOrderMqTemplate,
                           AccountDeductionMqTemplate accountDeductionMqTemplate) {
        this.cancelOrderMqTemplate = cancelOrderMqTemplate;
        this.accountDeductionMqTemplate = accountDeductionMqTemplate;
    }

    /**
     * 取消订单消息
     *
     * @param msgId   消息ID
     * @param orderNo 订单编号
     */
    public boolean sendCancelOrderMsg(String msgId, String orderNo) {
        JSONObject object = new JSONObject();
        object.put("orderNo", orderNo);
        Message message = MessageBuilder.withPayload(object.toJSONString()).setHeader(MESSAGE_ID_KEY, msgId).build();
        try {
            cancelOrderMqTemplate.sendMessageInTransaction(CANCEL_ORDER_TOPIC, message, null);
            return true;
        } catch (Exception e) {
            log.error("[Product]-[ProductProducer]-sendCancelOrderMsg err! orderNo:{}, msg:{}",
                    orderNo,
                    Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    /**
     * 用户扣款消息
     *
     * @param msgId   消息ID
     * @param orderNo 订单编号
     * @param userId  用户ID
     * @param amount  订单金额
     */
    public boolean sendAccountDeductionMsg(String msgId, String orderNo, String userId, BigDecimal amount) {
        JSONObject object = new JSONObject();
        AccountDeductionMsg msg = AccountDeductionMsg.builder().userId(userId).orderNo(orderNo).amount(amount).build();
        object.put("AccountDeductionMsg", msg);
        Message message = MessageBuilder.withPayload(object.toJSONString()).setHeader(MESSAGE_ID_KEY, msgId).build();
        try {
            accountDeductionMqTemplate.sendMessageInTransaction(ACCOUNT_DEDUCTION_TOPIC, message, null);
            return true;
        } catch (Exception e) {
            log.error("[Product]-[ProductProducer]-sendAccountDeductionMsg err! orderNo:{}, msg:{}",
                    orderNo,
                    Throwables.getStackTraceAsString(e));
            return false;
        }
    }

}
