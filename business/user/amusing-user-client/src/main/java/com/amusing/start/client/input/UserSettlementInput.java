package com.amusing.start.client.input;

import lombok.Data;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@Data
public class UserSettlementInput {

    /**
     * 用户唯一ID
     */
    private String userId;

    /**
     * 账户类型
     */
    private Integer amountType;

    /**
     * 结算金额
     */
    private String amount;
}
