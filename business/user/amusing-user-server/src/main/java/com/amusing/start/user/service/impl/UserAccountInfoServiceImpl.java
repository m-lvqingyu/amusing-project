package com.amusing.start.user.service.impl;

import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.enums.UserCode;
import com.amusing.start.user.mapper.UserAccountInfoMapper;
import com.amusing.start.user.pojo.UserAccountInfo;
import com.amusing.start.user.service.UserAccountInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@Service
public class UserAccountInfoServiceImpl implements UserAccountInfoService {

    private final UserAccountInfoMapper userAccountInfoMapper;

    @Autowired
    public UserAccountInfoServiceImpl(UserAccountInfoMapper userAccountInfoMapper) {
        this.userAccountInfoMapper = userAccountInfoMapper;
    }

    @Override
    public UserAccountOutput account(String userId) {
        UserAccountInfo info = userAccountInfoMapper.selectByUserId(userId);
        if (info == null) {
            return null;
        }
        UserAccountOutput output = new UserAccountOutput();
        BeanUtils.copyProperties(info, output);
        return output;
    }

    @Override
    public ApiResult userMainSettlement(String userId, String amount) {
        UserAccountInfo info = userAccountInfoMapper.selectByUserId(userId);
        if (info == null) {
            return ApiResult.fail(UserCode.USER_INFORMATION_NOT_EXIST);
        }

        BigDecimal mainAmount = info.getMainAmount();
        if (mainAmount == null) {
            return ApiResult.fail(UserCode.USER_INFORMATION_NOT_EXIST);
        }

        BigDecimal updateAmount = new BigDecimal(amount);
        if (mainAmount.compareTo(updateAmount) < 0) {
            return ApiResult.fail(UserCode.USER_AMOUNT_INSUFFICIENT_ERROR);
        }

        int result = userAccountInfoMapper.updateMainAccount(userId, mainAmount, updateAmount);
        if (result <= 0) {
            return ApiResult.fail(CommCode.FAIL);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult userGiveSettlement(String userId, String amount) {
        UserAccountInfo info = userAccountInfoMapper.selectByUserId(userId);
        if (info == null) {
            return ApiResult.fail(UserCode.USER_INFORMATION_NOT_EXIST);
        }

        BigDecimal giveAmount = info.getGiveAmount();
        if (giveAmount == null) {
            return ApiResult.fail(UserCode.USER_INFORMATION_NOT_EXIST);
        }

        BigDecimal updateAmount = new BigDecimal(amount);
        if (giveAmount.compareTo(updateAmount) < 0) {
            return ApiResult.fail(UserCode.USER_AMOUNT_INSUFFICIENT_ERROR);
        }

        int result = userAccountInfoMapper.updateGiveAccount(userId, giveAmount, updateAmount);
        if (result <= 0) {
            return ApiResult.fail(CommCode.FAIL);
        }
        return ApiResult.ok();
    }

}
