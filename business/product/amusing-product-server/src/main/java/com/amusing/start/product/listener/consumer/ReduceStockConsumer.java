package com.amusing.start.product.listener.consumer;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 扣减库存消费者
 * @date 2021/11/3 21:34
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "reduce_stock", consumerGroup = "ReduceStockGroup")
public class ReduceStockConsumer implements RocketMQListener<Message> {

    private static final String MESSAGE_ID_KEY = "msgUid";

    private static final String ORDER_NO_KEY = "SimpleOrderInfo";

    @Override
    public void onMessage(Message message) {
        String msgId = message.getHeaders().get(MESSAGE_ID_KEY, String.class);

        String str = new String((byte[]) message.getPayload());
        JSONObject object = JSONObject.parseObject(str);
        log.info("[Product]-[ReduceStockConsumer]-message:{}", object);
        if (object == null) {
            log.warn("[Product]-[ReduceStockConsumer]-message payload is null");
            return;
        }
        JSONObject jsonObject = object.getJSONObject(ORDER_NO_KEY);
        if (jsonObject == null) {
            log.warn("[Product]-[ReduceStockConsumer]-message body is null! payload:{}", object);
            return;
        }

        /**
         *  private String userId;
         *
         *     private String shopsId;
         *
         *     private String productId;
         *
         *     private String priceId;
         *
         *     private BigDecimal amount;
         *
         *     private String orderNo;
         */

    }

}
