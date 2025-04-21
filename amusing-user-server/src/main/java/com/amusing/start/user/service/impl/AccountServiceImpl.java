package com.amusing.start.user.service.impl;

import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.user.enums.UserErrorCode;
import com.amusing.start.user.mapper.AccountInfoMapper;
import com.amusing.start.user.entity.pojo.AccountInfo;
import com.amusing.start.user.service.AccountService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.seata.spring.annotation.GlobalLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Lv.QingYu
 * @since 2021/09/21
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountInfoMapper accountInfoMapper;

    @Override
    public void save(AccountInfo accountInfo) {
        if (accountInfoMapper.insert(accountInfo) <= 0) {
            throw new CustomException(CommunalCode.SERVICE_ERR);
        }
    }

    @Override
    public AccountInfo get(String userId) {
        return accountInfoMapper.selectOne(new LambdaQueryWrapper<AccountInfo>().eq(AccountInfo::getUserId, userId));
    }

    @GlobalLock(lockRetryInterval = 3, lockRetryTimes = 100)
    @Override
    public AccountInfo getByLock(String userId) {
        return get(userId);
    }

    @Override
    public Boolean deductions(String userId, Integer origAmount, Integer amount) {
        AccountInfo accountInfo = get(userId);
        if (accountInfo == null) {
            throw new CustomException(UserErrorCode.USER_NOT_FOUND);
        }
        AccountInfo updateAccount = new AccountInfo();
        updateAccount.setUpdateTime(System.currentTimeMillis());
        updateAccount.setMainAmount(origAmount - amount);
        LambdaUpdateWrapper<AccountInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AccountInfo::getMainAmount, origAmount);
        updateWrapper.eq(AccountInfo::getUserId, userId);
        return accountInfoMapper.update(updateAccount, updateWrapper) > 0;
    }

}
