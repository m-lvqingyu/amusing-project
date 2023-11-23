package com.amusing.start.user.service;

import com.amusing.start.user.entity.pojo.AccountInfo;

/**
 * @author Lv.QingYu
 * @description: 账户Service
 * @since 2021/09/21
 */
public interface AccountService {

    /**
     * @param userId 用户ID
     * @return 账户详情
     * @description: 获取账户详情(无锁)
     */
    AccountInfo getById(String userId);

    /**
     * @param userId 用户ID
     * @return 账户详情
     * @description: 获取账户详情（有锁）
     */
    AccountInfo getByIdLock(String userId);

    /**
     * @param accountInfo 账号信息
     * @return 账户ID
     * @description: 保存账户信息
     */
    Integer insert(AccountInfo accountInfo);

    /**
     * @param userId     用户ID
     * @param mainAmount 主账户金额
     * @param amount     扣减金额
     * @return 大于0：成功 小于或等于0：失败
     * @description: 主账户金额扣减
     */
    Integer updateMainAccount(String userId, Integer mainAmount, Integer amount);

}
