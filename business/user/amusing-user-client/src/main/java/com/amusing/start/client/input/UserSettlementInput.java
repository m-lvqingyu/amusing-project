package com.amusing.start.client.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSettlementInput {

    /**
     * 用户唯一ID
     */
    private String userId;

    /**
     * 结算金额
     */
    private BigDecimal amount;

}
