package com.amusing.start.order.listener.transactional.product;

import com.alibaba.fastjson.JSONObject;
import com.amusing.start.order.enums.OrderStatus;
import com.amusing.start.order.mapper.OrderInfoMapper;
import com.amusing.start.order.message.OrderCreateMsg;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@Slf4j
@Component
@RocketMQTransactionListener(rocketMQTemplateBeanName = "reduceStockMqTemplate")
public class ReduceStockListener implements RocketMQLocalTransactionListener {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    private static final String ORDER_NO_KEY = "SimpleOrderInfo";

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String orderNo = orderNo(msg);
        if (StringUtils.isEmpty(orderNo)) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        Integer orderStatus = null;
        try {
            orderStatus = orderInfoMapper.selectOrderStatus(orderNo);
        } catch (Exception e) {
            log.error("[Order]-[create]-executeLocalTransaction queryOrderStatus err! orderNo:{}, msg:{}", orderNo, Throwables.getStackTraceAsString(e));
        }
        log.info("[Order]-[create]-executeLocalTransaction orderNo:{}, orderStatus:{}", orderNo, orderStatus);
        if (orderStatus == null) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        if (OrderStatus.SCHEDULED.getKey() != orderStatus) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        int result = 0;
        try {
            result = orderInfoMapper.updateOrderStatus(orderNo, OrderStatus.REDUCE_STOCK.getKey());
        } catch (Exception e) {
            log.error("[Order]-[create]-executeLocalTransaction updateOrderStatus err! orderNo:{}, msg:{}", orderNo, Throwables.getStackTraceAsString(e));
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        if (result <= 0) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        return RocketMQLocalTransactionState.COMMIT;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        String orderNo = orderNo(msg);
        if (StringUtils.isEmpty(orderNo)) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        Integer orderStatus = null;
        try {
            orderStatus = orderInfoMapper.selectOrderStatus(orderNo);
        } catch (Exception e) {
            log.error("[Order]-[create]-checkLocalTransaction queryOrderStatus err! orderNo:{}, msg:{}", orderNo, Throwables.getStackTraceAsString(e));
        }
        log.info("[Order]-[create]-checkLocalTransaction orderNo:{}, orderStatus:{}", orderNo, orderStatus);
        if (orderStatus == null) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        if (OrderStatus.REDUCE_STOCK.getKey() <= orderStatus) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }

    private String orderNo(Message message) {
        String str = new String((byte[]) message.getPayload());
        JSONObject object = JSONObject.parseObject(str);
        OrderCreateMsg createMsg = object.getObject(ORDER_NO_KEY, OrderCreateMsg.class);
        if (createMsg == null) {
            return "";
        }
        return createMsg.getOrderNo();
    }

}
