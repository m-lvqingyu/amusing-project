package com.amusing.start.user.service.impl;

import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.user.constant.UserConstant;
import com.amusing.start.user.enums.UserCode;
import com.amusing.start.user.exception.UserException;
import com.amusing.start.user.mapper.UserAccountInfoMapper;
import com.amusing.start.user.pojo.UserAccountInfo;
import com.amusing.start.user.service.IUserAccountInfoService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@Slf4j
@Service
public class UserAccountInfoServiceImpl implements IUserAccountInfoService {

    private final UserAccountInfoMapper userAccountInfoMapper;

    @Autowired
    public UserAccountInfoServiceImpl(UserAccountInfoMapper userAccountInfoMapper) {
        this.userAccountInfoMapper = userAccountInfoMapper;
    }

    @Override
    public UserAccountOutput account(String userId) {
        UserAccountOutput output = new UserAccountOutput();
        UserAccountInfo info = userAccountInfoMapper.selectByUserId(userId);
        Optional.ofNullable(info).ifPresent(i -> {
            BeanUtils.copyProperties(info, output);
        });
        return output;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean userMainSettlement(String userId, BigDecimal amount) throws UserException {
        // 根据用户ID,获取其主账户余额，获取不到则抛出异常
        UserAccountInfo info = userAccountInfoMapper.selectByUserId(userId);
        BigDecimal mainAmount = Optional.ofNullable(info).map(UserAccountInfo::getMainAmount)
                .orElseThrow(() -> new UserException(UserCode.USER_INFORMATION_NOT_EXIST));

        // 判断用户主账户余额是否足够，不足则抛出异常
        BigDecimal updateAmount = Optional.ofNullable(amount).filter(i -> i.compareTo(mainAmount) < UserConstant.ZERO)
                .orElseThrow(() -> new UserException(UserCode.USER_AMOUNT_INSUFFICIENT_ERROR));

        // 更新用户主账户余额，更新失败抛出异常
        Integer result = null;
        try {
            result = userAccountInfoMapper.updateMainAccount(userId, mainAmount, updateAmount);
        } catch (Exception e) {
            log.error("[user]-updateMainAccount err! userId:{}, amount:{}, msg:{}", userId, amount, Throwables.getStackTraceAsString(e));
        }
        Optional.ofNullable(result).filter(i -> i > UserConstant.ZERO).orElseThrow(() -> new UserException(CommCode.FAIL));
        return UserConstant.TRUE;
    }

    @Override
    public boolean userGiveSettlement(String userId, BigDecimal amount) throws UserException {
        // 根据用户ID,获取其副账户余额，获取不到则抛出异常
        UserAccountInfo info = userAccountInfoMapper.selectByUserId(userId);
        BigDecimal giveAmount = Optional.ofNullable(info).map(UserAccountInfo::getGiveAmount)
                .orElseThrow(() -> new UserException(UserCode.USER_INFORMATION_NOT_EXIST));

        // 判断用户副账户余额是否足够，不足则抛出异常
        BigDecimal updateAmount = Optional.ofNullable(amount).filter(i -> i.compareTo(giveAmount) > UserConstant.ZERO)
                .orElseThrow(() -> new UserException(UserCode.USER_AMOUNT_INSUFFICIENT_ERROR));

        // 更新用户副账户余额，更新失败抛出异常
        Integer result = null;
        try {
            result = userAccountInfoMapper.updateGiveAccount(userId, giveAmount, updateAmount);
        } catch (Exception e) {
            log.error("[user]-updateGiveAccount err! userId:{}, amount:{}, msg:{}", userId, amount, Throwables.getStackTraceAsString(e));
        }
        Optional.ofNullable(result).filter(i -> i > UserConstant.ZERO).orElseThrow(() -> new UserException(CommCode.FAIL));
        return UserConstant.TRUE;
    }

    @Override
    public boolean init(String userId) throws UserException {
        String autoId = userAccountInfoMapper.checkAmountIsExist(userId);
        if (StringUtils.isNotEmpty(autoId)) {
            log.error("[user]-initAccount userId is exist! userId:{}", userId);
            throw new UserException(UserCode.INSERT_ERR);
        }
        long currentTime = System.currentTimeMillis();
        UserAccountInfo accountInfo = UserAccountInfo.builder()
                .userId(userId)
                .mainAmount(BigDecimal.ZERO)
                .giveAmount(BigDecimal.ZERO)
                .frozenAmount(BigDecimal.ZERO)
                .vipLevel(UserConstant.ZERO)
                .createBy(userId)
                .createTime(currentTime)
                .updateBy(userId)
                .updateTime(currentTime)
                .build();
        Integer result = null;
        try {
            result = userAccountInfoMapper.insertSelective(accountInfo);
        } catch (Exception e) {
            log.error("[user]-initAccount err! userId:{}, msg:{}", userId, Throwables.getStackTraceAsString(e));
        }
        Optional.ofNullable(result).filter(i -> i > UserConstant.ZERO).orElseThrow(() -> new UserException(UserCode.INSERT_ERR));
        return true;
    }

}
