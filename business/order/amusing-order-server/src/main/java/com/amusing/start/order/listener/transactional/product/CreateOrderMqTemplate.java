package com.amusing.start.order.listener.transactional.product;

import org.apache.rocketmq.spring.annotation.ExtRocketMQTemplateConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

/**
 * Create By 2021/11/13
 *
 * @author lvqingyu
 */
@ExtRocketMQTemplateConfiguration(nameServer = "${rocketmq.name-server}", group="CreateOrderGroup")
public class CreateOrderMqTemplate extends RocketMQTemplate {
}
