package com.amusing.start.product.listener.consumer;

import com.alibaba.fastjson.JSONObject;
import com.amusing.start.product.enums.MessageStatus;
import com.amusing.start.product.enums.ResultStatus;
import com.amusing.start.product.listener.message.ReduceStockMsg;
import com.amusing.start.product.pojo.ProductInfo;
import com.amusing.start.product.pojo.ProductMessageInfo;
import com.amusing.start.product.service.IProductMessageInfoService;
import com.amusing.start.product.service.IProductService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
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
@RocketMQMessageListener(topic = "create_order", consumerGroup = "ReduceStockGroup")
public class ReduceStockConsumer implements RocketMQListener<Message> {

    private final IProductService productService;
    private final IProductMessageInfoService productMessageInfoService;

    @Value("${product.worker}")
    private Long productWorker;

    @Value("${product.dataCenter}")
    private Long productDataCenter;

    @Autowired
    public ReduceStockConsumer(IProductService productService,
                               IProductMessageInfoService productMessageInfoService) {
        this.productService = productService;
        this.productMessageInfoService = productMessageInfoService;
    }

    @Override
    public void onMessage(Message message) {
        String txId = getTxId(message);
        if (StringUtils.isEmpty(txId)) {
            log.warn("[Product]-[ReduceStockConsumer]-txId is null");
            return;
        }
        ReduceStockMsg reduceStockMsg = analysisBody(txId, message);
        if (reduceStockMsg == null) {
            log.warn("[Product]-[ReduceStockConsumer]-message body format error! txId:{}", txId);
            return;
        }

        ProductMessageInfo productMessageInfo = buildProductMessageInfo(txId, reduceStockMsg);
        try {
            productMessageInfoService.save(productMessageInfo);
        } catch (Exception e) {
            log.error("[Product]-[ReduceStockConsumer]-productMessageInfo save error! txId:{}", txId);
            return;
        }

        // 判断商品库存是否足够
        boolean checkProductStockResult = checkProductStock(productMessageInfo);
        if (!checkProductStockResult) {
            productMessageInfoService.updateStatus(txId, MessageStatus.PROCESSED.getCode(), ResultStatus.FAIL.getCode());
        } else {
            String shopId = productMessageInfo.getShopId();
            String productId = productMessageInfo.getProductId();
            Integer productNum = productMessageInfo.getProductNum();
            boolean productStock = false;
            try {
                productStock = productService.deductionProductStock(txId, shopId, productId, productNum);
            } catch (Exception e) {
                log.error("[Product]-[ReduceStockConsumer]-productMessageInfo save error! txId:{}", txId);
                productMessageInfoService.updateStatus(txId, MessageStatus.PROCESSED.getCode(), ResultStatus.FAIL.getCode());
            }
        }
    }

    /**
     * 获得消息ID
     *
     * @param message
     * @return
     */
    private String getTxId(Message message) {
        String txId = message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID, String.class);
        return txId;
    }

    /**
     * 扣减库存消息体解析
     *
     * @param message
     * @return
     */
    private ReduceStockMsg analysisBody(String txId, Message message) {
        Object payload = message.getPayload();
        if (payload == null) {
            log.warn("[Product]-[ReduceStockConsumer]-message payload is null! txId:{}", txId);
            return null;
        }

        String str = new String((byte[]) payload);
        ReduceStockMsg reduceStockMsg = null;
        try {
            reduceStockMsg = JSONObject.parseObject(str, ReduceStockMsg.class);
        } catch (Exception e) {
            log.error("[Product]-[ReduceStockConsumer]-message body format err! txId:{}, msg:{}",
                    txId,
                    Throwables.getStackTraceAsString(e));
        }
        log.info("[Product]-[ReduceStockConsumer]-txId:{}, reduceStockMsg:{}", txId, reduceStockMsg);
        return reduceStockMsg;
    }

    private ProductMessageInfo buildProductMessageInfo(String txId, ReduceStockMsg reduceStockMsg) {
        Date currentTime = new Date();
        return ProductMessageInfo.builder()
                .txId(txId)
                .status(MessageStatus.INIT.getCode())
                .resultStatus(ResultStatus.INIT.getCode())
                .shopId(reduceStockMsg.getShopsId())
                .productId(reduceStockMsg.getProductId())
                .priceId(reduceStockMsg.getPriceId())
                .productNum(reduceStockMsg.getProductNum())
                .amount(reduceStockMsg.getAmount())
                .createTime(currentTime)
                .updateTime(currentTime)
                .build();
    }

    /**
     * 判断库存是否足够
     *
     * @param productMessageInfo
     * @return
     */
    private boolean checkProductStock(ProductMessageInfo productMessageInfo) {
        String shopsId = productMessageInfo.getShopId();
        String productId = productMessageInfo.getProductId();
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
        Integer productNum = productMessageInfo.getProductNum();
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

}
