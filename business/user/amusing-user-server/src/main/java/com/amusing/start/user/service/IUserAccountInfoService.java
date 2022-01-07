package com.amusing.start.user.service;

import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.user.exception.UserException;

import java.math.BigDecimal;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
public interface IUserAccountInfoService {

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
    boolean userMainSettlement(String userId, BigDecimal amount) throws UserException;

    /**
     * 更新副账户余额
     *
     * @param userId 用户ID
     * @param amount 支付金额
     * @return
     * @throws UserException
     */
    boolean userGiveSettlement(String userId, BigDecimal amount) throws UserException;

    /**
     * 初始化账户
     *
     * @param userId 用户ID
     * @return true:成功  false:失败
     * @throws UserException
     */
    boolean init(String userId) throws UserException;

}
