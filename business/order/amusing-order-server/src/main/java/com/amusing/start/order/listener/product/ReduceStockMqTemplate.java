package com.amusing.start.order.listener.product;

import org.apache.rocketmq.spring.annotation.ExtRocketMQTemplateConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@ExtRocketMQTemplateConfiguration(nameServer = "${reduce.stock.name.server}", group = "OrderCreateGroup")
@Component("reduceStockMqTemplate")
public class ReduceStockMqTemplate extends RocketMQTemplate {
}
