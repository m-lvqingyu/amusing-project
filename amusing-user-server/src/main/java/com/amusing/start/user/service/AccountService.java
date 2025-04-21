package com.amusing.start.user.service;

import com.amusing.start.user.entity.pojo.AccountInfo;

/**
 * @author Lv.QingYu
 * @since 2021/09/21
 */
public interface AccountService {

    /**
     * 保存账户信息
     *
     * @param accountInfo 账号信息
     */
    void save(AccountInfo accountInfo);

    /**
     * 获取账户详情(无锁)
     *
     * @param userId 用户ID
     * @return 账户详情
     */
    AccountInfo get(String userId);

    /**
     * 获取账户详情（有锁）
     *
     * @param userId 用户ID
     * @return 账户详情
     */
    AccountInfo getByLock(String userId);
    
    /**
     * 主账户金额扣减
     *
     * @param userId     用户ID
     * @param origAmount 账户余额
     * @param amount     扣减金额
     * @return true：成功 false：失败
     */
    Boolean deductions(String userId, Integer origAmount, Integer amount);

}
