package com.amusing.start.client.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountOutput {

    private String userId;

    /**
     * 主账户金额
     */
    private BigDecimal mainAmount;

    /**
     * 副账户金额
     */
    private BigDecimal giveAmount;

    /**
     * 冻结金额
     */
    private BigDecimal frozenAmount;

    /**
     * 会员等级
     */
    private Integer vipLevel;

}
