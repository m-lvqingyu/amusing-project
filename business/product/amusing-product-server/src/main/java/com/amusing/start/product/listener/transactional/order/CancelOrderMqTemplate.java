package com.amusing.start.product.listener.transactional.order;

import org.apache.rocketmq.spring.annotation.ExtRocketMQTemplateConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 取消订单MQTemplate
 * @date 2021/11/4 24:27
 */
@ExtRocketMQTemplateConfiguration(nameServer = "${rocketmq.name-server}", group = "CancelOrderGroup")
@Component("cancelOrderMqTemplate")
public class CancelOrderMqTemplate extends RocketMQTemplate {
}
