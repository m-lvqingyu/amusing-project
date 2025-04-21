package com.amusing.start.client.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AccountOutput {

    private String userId;

    /**
     * 主账户金额
     */
    private Integer mainAmount;

    /**
     * 副账户金额
     */
    private Integer giveAmount;

    /**
     * 冻结金额
     */
    private Integer frozenAmount;

    /**
     * 会员等级
     */
    private Integer vipLevel;

}
