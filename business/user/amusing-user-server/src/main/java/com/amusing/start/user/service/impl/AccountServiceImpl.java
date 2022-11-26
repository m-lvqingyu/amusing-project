package com.amusing.start.user.service.impl;

import com.amusing.start.client.output.AccountOutput;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.user.constant.UserConstant;
import com.amusing.start.user.entity.pojo.UserInfo;
import com.amusing.start.user.mapper.AccountInfoMapper;
import com.amusing.start.user.entity.pojo.AccountInfo;
import com.amusing.start.user.mapper.UserInfoMapper;
import com.amusing.start.user.service.IAccountService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public AccountOutput account(String userId) throws CustomException {
        AccountInfo accountInfo = getAccountInfo(userId);
        AccountOutput output = new AccountOutput();
        BeanUtils.copyProperties(accountInfo, output);
        return output;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean pay(String userId, BigDecimal amount) throws CustomException {
        AccountInfo accountInfo = getAccountInfo(userId);
        BigDecimal mainAmount = accountInfo.getMainAmount();
        if (mainAmount == null || amount.compareTo(mainAmount) > UserConstant.ZERO) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        try {
            Integer result = accountInfoMapper.updateMainAccount(userId, mainAmount, amount);
            return result > 0 ? UserConstant.TRUE : UserConstant.FALSE;
        } catch (Exception e) {
            log.error("[user]-updateMainAccount err! userId:{}, amount:{}, msg:{}",
                    userId,
                    amount,
                    Throwables.getStackTraceAsString(e));
            throw new CustomException(ErrorCode.PAY_ERR);
        }
    }

    private AccountInfo getAccountInfo(String userId) throws CustomException {
        UserInfo userInfo = userInfoMapper.getById(userId);
        if (userInfo == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        AccountInfo accountInfo = accountInfoMapper.selectById(userId);
        if (accountInfo == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return accountInfo;
    }

}
