package com.amusing.start.product.listener.transactional.account;

import org.apache.rocketmq.spring.annotation.ExtRocketMQTemplateConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 用户扣款MQ
 * @date 2021/11/4 22:20
 */
@ExtRocketMQTemplateConfiguration(nameServer = "${rocketmq.name-server}", group = "AccountDeductionGroup")
@Component("accountDeductionMqTemplate")
public class AccountDeductionMqTemplate extends RocketMQTemplate {
}
