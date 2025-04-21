package com.amusing.start.client.request;

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
public class AccountPayRequest {

    /**
     * 用户唯一ID
     */
    private String userId;
    /**
     * 结算金额
     */
    private Integer amount;
    /**
     * 账户余额
     */
    private Integer origAmount;

}
