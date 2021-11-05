package com.amusing.start.product.listener.consumer;

import com.alibaba.fastjson.JSONObject;
import com.amusing.start.product.enums.MessageStatus;
import com.amusing.start.product.listener.producer.ProductProducer;
import com.amusing.start.product.pojo.ProductInfo;
import com.amusing.start.product.pojo.ReduceStockMsgInfo;
import com.amusing.start.product.service.ProductService;
import com.amusing.start.product.service.ReduceStockMsgInfoService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

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

    private final ProductService productService;

    private final ProductProducer productProducer;

    private final ReduceStockMsgInfoService reduceStockMsgInfoService;

    @Autowired
    public ReduceStockConsumer(ProductService productService,
                               ProductProducer productProducer,
                               ReduceStockMsgInfoService reduceStockMsgInfoService) {
        this.productService = productService;
        this.productProducer = productProducer;
        this.reduceStockMsgInfoService = reduceStockMsgInfoService;
    }

    @Override
    public void onMessage(Message message) {
        ReduceStockMsgInfo reduceStockMsgInfo = analysisBody(message);
        if (reduceStockMsgInfo == null) {
            log.warn("[Product]-[ReduceStockConsumer]-message body format error!");
            return;
        }

        String msgId = reduceStockMsgInfo.getMsgId();
        ReduceStockMsgInfo msgInfo = reduceStockMsgInfoService.getMsgInfo(msgId);
        if (msgInfo != null) {
            log.warn("[Product]-[ReduceStockConsumer]-message already exists! msgId:{}", msgId);
            return;
        }
        // 消息入库
        try {
            reduceStockMsgInfoService.saveReduceStockMsgInfo(reduceStockMsgInfo);
        } catch (Exception e) {
            log.error("[Product]-[ReduceStockConsumer]-save message err! msgId:{}, msg:{}", msgId, Throwables.getStackTraceAsString(e));
            return;
        }

        // 判断商品库存是否足够
        String orderNo = reduceStockMsgInfo.getOrderNo();
        boolean checkProductStockResult = checkProductStock(reduceStockMsgInfo);
        if (!checkProductStockResult) {
            productProducer.sendCancelOrderMsg(msgId, orderNo);
        } else {
            String userId = reduceStockMsgInfo.getUserId();
            BigDecimal amount = reduceStockMsgInfo.getAmount();
            productProducer.sendAccountDeductionMsg(msgId, orderNo, userId, amount);
        }
    }

    /**
     * 扣减库存消息体解析
     *
     * @param message
     * @return
     */
    private ReduceStockMsgInfo analysisBody(Message message) {
        MessageHeaders headers = message.getHeaders();
        if (headers == null) {
            log.warn("[Product]-[ReduceStockConsumer]-MessageHeaders is null");
            return null;
        }
        String msgId = headers.get(MESSAGE_ID_KEY, String.class);
        if (StringUtils.isEmpty(msgId)) {
            log.warn("[Product]-[ReduceStockConsumer]-msgId is null");
            return null;
        }
        Object payload = message.getPayload();
        if (payload == null) {
            log.warn("[Product]-[ReduceStockConsumer]-message payload is null! msgId:{}", msgId);
            return null;
        }

        String str = new String((byte[]) payload);
        JSONObject object = null;
        try {
            object = JSONObject.parseObject(str);
        } catch (Exception e) {
            log.error("[Product]-[ReduceStockConsumer]-message body format err! msgId:{}, msg:{}",
                    msgId,
                    Throwables.getStackTraceAsString(e));
        }
        log.info("[Product]-[ReduceStockConsumer]-msgId:{}, message:{}", msgId, object);
        if (object == null) {
            log.warn("[Product]-[ReduceStockConsumer]-message payload is null! msgId:{}", msgId);
            return null;
        }

        JSONObject jsonObject = object.getJSONObject(ORDER_NO_KEY);
        if (jsonObject == null) {
            log.warn("[Product]-[ReduceStockConsumer]-message body is null! msgId:{} payload:{}", msgId, object);
            return null;
        }
        ReduceStockMsgInfo reduceStockMsgInfo = checkMessage(msgId, jsonObject);
        return reduceStockMsgInfo;
    }

    /**
     * 判断库存是否足够
     *
     * @param reduceStockMsgInfo
     * @return
     */
    private boolean checkProductStock(ReduceStockMsgInfo reduceStockMsgInfo) {
        String shopsId = reduceStockMsgInfo.getShopsId();
        String productId = reduceStockMsgInfo.getProductId();
        ProductInfo productInfo = null;
        try {
            productInfo = productService.getProductInfo(shopsId, productId);
        } catch (Exception e) {
            log.error("[Product]-[ReduceStockConsumer]-getProductInfo err! shopId:{}, productId:{}, msg:{}",
                    shopsId,
                    productId,
                    Throwables.getStackTraceAsString(e));
        }
        if (productInfo == null) {
            log.warn("[Product]-[ReduceStockConsumer]-ProductInfo does not exist! shopId:{}, productId:{}",
                    shopsId,
                    productId);
            return false;
        }
        BigDecimal productStock = productInfo.getProductStock();
        if (productStock == null || productStock.compareTo(BigDecimal.ZERO) < -1) {
            log.warn("[Product]-[ReduceStockConsumer]-ProductInfo insufficient inventory! shopId:{}, productId:{}, productStock:{}",
                    shopsId,
                    productId,
                    productStock);
            return false;
        }
        Integer productNum = reduceStockMsgInfo.getProductNum();
        if (new BigDecimal(productNum).compareTo(productStock) < -1) {
            log.warn("[Product]-[ReduceStockConsumer]-ProductInfo insufficient inventory! shopId:{}, productId:{}, productStock:{}, productNum:{}",
                    shopsId,
                    productId,
                    productStock,
                    productNum);
            return false;
        }
        return true;
    }

    private ReduceStockMsgInfo checkMessage(String msgId, JSONObject jsonObject) {
        String userId = jsonObject.getObject("userId", String.class);
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        String shopsId = jsonObject.getObject("shopsId", String.class);
        if (StringUtils.isEmpty(shopsId)) {
            return null;
        }
        String productId = jsonObject.getObject("productId", String.class);
        if (StringUtils.isEmpty(productId)) {
            return null;
        }
        String priceId = jsonObject.getObject("priceId", String.class);
        if (StringUtils.isEmpty(priceId)) {
            return null;
        }
        BigDecimal amount = jsonObject.getObject("amount", BigDecimal.class);
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < -1) {
            return null;
        }
        String orderNo = jsonObject.getObject("orderNo", String.class);
        if (StringUtils.isEmpty(orderNo)) {
            return null;
        }
        Integer productNum = jsonObject.getObject("productNum", Integer.class);
        if (productNum == null || productNum <= 0) {
            return null;
        }
        Date currentTime = new Date();
        return ReduceStockMsgInfo.builder()
                .msgId(msgId)
                .userId(userId)
                .shopsId(shopsId)
                .productId(productId)
                .productNum(productNum)
                .priceId(priceId)
                .amount(amount)
                .orderNo(orderNo)
                .status(MessageStatus.INIT.getCode())
                .createTime(currentTime)
                .updateTime(currentTime)
                .build();
    }

}
