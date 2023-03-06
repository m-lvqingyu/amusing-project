package com.amusing.start.user.service.impl;

import com.amusing.start.client.output.AccountOutput;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.user.constant.UserConstant;
import com.amusing.start.user.mapper.AccountInfoMapper;
import com.amusing.start.user.entity.pojo.AccountInfo;
import com.amusing.start.user.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@Slf4j
@Service
public class AccountServiceImpl implements IAccountService {

    @Resource
    private AccountInfoMapper accountInfoMapper;

    @Override
    public AccountOutput account(String userId) throws CustomException {
        AccountInfo accountInfo = getAccountInfo(userId);
        return AccountOutput.builder().userId(accountInfo.getUserId())
                .mainAmount(accountInfo.getMainAmount())
                .giveAmount(accountInfo.getGiveAmount())
                .frozenAmount(accountInfo.getFrozenAmount())
                .vipLevel(accountInfo.getVipLevel())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean payment(String userId, BigDecimal amount) throws CustomException {
        AccountInfo accountInfo = getAccountInfo(userId);
        BigDecimal mainAmount = accountInfo.getMainAmount();
        if (mainAmount == null || amount.compareTo(mainAmount) > UserConstant.ZERO) {
            throw new CustomException(ErrorCode.ACCOUNT_INSUFFICIENT);
        }
        Integer result = accountInfoMapper.updateMainAccount(userId, mainAmount, amount);
        if (result == null || result <= UserConstant.ZERO) {
            throw new CustomException(ErrorCode.PAY_ERR);
        }
        return UserConstant.TRUE;
    }

    private AccountInfo getAccountInfo(String userId) throws CustomException {
        AccountInfo accountInfo = accountInfoMapper.selectById(userId);
        if (accountInfo == null) {
            throw new CustomException(ErrorCode.ACCOUNT_FROZEN_ERR);
        }
        return accountInfo;
    }

}
