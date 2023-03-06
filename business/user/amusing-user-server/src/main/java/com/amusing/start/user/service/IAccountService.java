package com.amusing.start.user.service;

import com.amusing.start.client.output.AccountOutput;
import com.amusing.start.exception.CustomException;

import java.math.BigDecimal;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
public interface IAccountService {

    /**
     * 根据用户ID获取用户账户详情
     *
     * @param userId
     * @return
     */
    AccountOutput account(String userId) throws CustomException;

    /**
     * 更新主账户余额
     *
     * @param userId 用户ID
     * @param amount 支付金额
     * @return
     * @throws CustomException
     */
    Boolean payment(String userId, BigDecimal amount) throws CustomException;

}
