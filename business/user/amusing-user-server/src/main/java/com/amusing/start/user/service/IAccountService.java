package com.amusing.start.user.service;

import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.user.exception.UserException;

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
    UserAccountOutput account(String userId);

    /**
     * 更新主账户余额
     *
     * @param userId 用户ID
     * @param amount 支付金额
     * @return
     * @throws UserException
     */
    Boolean mainSettlement(String userId, BigDecimal amount);

    /**
     * 初始化账户
     *
     * @param userId 用户ID
     * @return true:成功  false:失败
     * @throws UserException
     */
    Boolean init(String userId);

}
