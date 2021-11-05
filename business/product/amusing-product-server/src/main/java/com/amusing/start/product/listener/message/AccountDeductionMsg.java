package com.amusing.start.product.listener.message;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 用户扣款消息体
 * @date 2021/11/4 22:08
 */
@Data
@Builder
public class AccountDeductionMsg {

    private String userId;

    private String orderNo;

    private BigDecimal amount;

}
