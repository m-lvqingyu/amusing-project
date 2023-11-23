package com.amusing.start.user.service.impl;

import com.amusing.start.user.mapper.AccountInfoMapper;
import com.amusing.start.user.entity.pojo.AccountInfo;
import com.amusing.start.user.service.AccountService;
import io.seata.spring.annotation.GlobalLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Lv.QingYu
 * @description: 用户ServiceImpl
 * @since 2021/09/21
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountInfoMapper accountInfoMapper;

    @Override
    public AccountInfo getById(String userId) {
        return accountInfoMapper.getByIdLock(userId);
    }

    @GlobalLock(lockRetryInterval = 3, lockRetryTimes = 100)
    @Override
    public AccountInfo getByIdLock(String userId) {
        return accountInfoMapper.getByIdLock(userId);
    }

    @Override
    public Integer insert(AccountInfo accountInfo) {
        return accountInfoMapper.insert(accountInfo);
    }

    @Override
    public Integer updateMainAccount(String userId, Integer mainAmount, Integer amount) {
        return accountInfoMapper.updateMainAccount(userId, mainAmount, amount);
    }

}
