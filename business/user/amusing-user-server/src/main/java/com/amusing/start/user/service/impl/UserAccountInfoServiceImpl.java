package com.amusing.start.user.service.impl;

import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.result.ApiCode;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.enums.AmountType;
import com.amusing.start.user.mapper.UserAccountInfoMapper;
import com.amusing.start.user.mapper.plus.UserAccountInfoMapperPlus;
import com.amusing.start.user.pojo.UserAccountInfo;
import com.amusing.start.user.pojo.UserAccountInfoExample;
import com.amusing.start.user.service.UserAccountInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@Service
public class UserAccountInfoServiceImpl implements UserAccountInfoService {

    private final UserAccountInfoMapper userAccountInfoMapper;
    private final UserAccountInfoMapperPlus userAccountInfoMapperPlus;

    @Autowired
    public UserAccountInfoServiceImpl(UserAccountInfoMapper userAccountInfoMapper,
                                      UserAccountInfoMapperPlus userAccountInfoMapperPlus) {
        this.userAccountInfoMapper = userAccountInfoMapper;
        this.userAccountInfoMapperPlus = userAccountInfoMapperPlus;
    }

    @Override
    public UserAccountOutput account(String userId) {
        UserAccountInfo info = getInfo(userId);
        if (info == null) {
            return null;
        }
        UserAccountOutput output = new UserAccountOutput();
        BeanUtils.copyProperties(info, output);
        return output;
    }

    @Override
    public ApiResult userMainSettlement(String userId, String amount) {
        UserAccountInfo info = getInfo(userId);
        if (info == null) {
            return ApiResult.fail(ApiCode.USER_INFORMATION_NOT_EXIST);
        }

        BigDecimal mainAmount = info.getMainAmount();
        if(mainAmount == null){
            return ApiResult.fail(ApiCode.USER_INFORMATION_NOT_EXIST);
        }

        BigDecimal updateAmount = new BigDecimal(amount);
        if (mainAmount.compareTo(updateAmount) < 0) {
            return ApiResult.fail(ApiCode.USER_AMOUNT_INSUFFICIENT_ERROR);
        }

        int result = userAccountInfoMapperPlus.updateMainAccount(userId, mainAmount, updateAmount);
        if (result <= 0) {
            return ApiResult.fail(ApiCode.FAIL);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult userGiveSettlement(String userId, String amount) {
        UserAccountInfo info = getInfo(userId);
        if (info == null) {
            return ApiResult.fail(ApiCode.USER_INFORMATION_NOT_EXIST);
        }

        BigDecimal giveAmount = info.getGiveAmount();
        if(giveAmount == null){
            return ApiResult.fail(ApiCode.USER_INFORMATION_NOT_EXIST);
        }

        BigDecimal updateAmount = new BigDecimal(amount);
        if (giveAmount.compareTo(updateAmount) < 0) {
            return ApiResult.fail(ApiCode.USER_AMOUNT_INSUFFICIENT_ERROR);
        }

        int result = userAccountInfoMapperPlus.updateGiveAccount(userId, giveAmount, updateAmount);
        if (result <= 0) {
            return ApiResult.fail(ApiCode.FAIL);
        }
        return ApiResult.ok();
    }

    private UserAccountInfo getInfo(String userId) {
        UserAccountInfoExample example = new UserAccountInfoExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<UserAccountInfo> infoList = userAccountInfoMapper.selectByExample(example);
        if (infoList == null || infoList.isEmpty()) {
            return null;
        }
        return infoList.get(0);
    }

}
