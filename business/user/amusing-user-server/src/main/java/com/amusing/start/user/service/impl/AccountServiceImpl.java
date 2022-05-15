package com.amusing.start.user.service.impl;

import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.user.constant.UserConstant;
import com.amusing.start.user.mapper.UserAccountInfoMapper;
import com.amusing.start.user.pojo.UserAccountInfo;
import com.amusing.start.user.service.IAccountService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
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
public class AccountServiceImpl implements IAccountService {

    private final UserAccountInfoMapper userAccountInfoMapper;

    private final RedissonClient redissonClient;

    @Autowired
    public AccountServiceImpl(UserAccountInfoMapper userAccountInfoMapper, RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
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
    public Boolean mainSettlement(String userId, BigDecimal amount) {
        // 根据用户ID,获取其主账户余额，获取不到则抛出异常
        UserAccountInfo info = userAccountInfoMapper.selectByUserId(userId);
        if (info == null) {
            return UserConstant.FALSE;
        }
        BigDecimal mainAmount = info.getMainAmount();
        if (mainAmount == null || amount.compareTo(mainAmount) > UserConstant.ZERO) {
            return UserConstant.FALSE;
        }
        // 更新用户主账户余额，更新失败抛出异常
        Integer result = null;
        try {
            result = userAccountInfoMapper.updateMainAccount(userId, mainAmount, amount);
        } catch (Exception e) {
            log.error("[user]-updateMainAccount err! userId:{}, amount:{}, msg:{}", userId, amount, Throwables.getStackTraceAsString(e));
        }
        return result == null ? UserConstant.FALSE : UserConstant.TRUE;
    }

    @Override
    public Boolean giveSettlement(String userId, BigDecimal amount) {
        // 根据用户ID,获取其副账户余额，获取不到则抛出异常
        UserAccountInfo info = userAccountInfoMapper.selectByUserId(userId);
        if (info == null) {
            return UserConstant.FALSE;
        }
        BigDecimal giveAmount = info.getGiveAmount();
        if (giveAmount == null || amount.compareTo(giveAmount) < UserConstant.ZERO) {
            return UserConstant.FALSE;
        }
        // 更新用户副账户余额，更新失败抛出异常
        Integer result = null;
        try {
            result = userAccountInfoMapper.updateGiveAccount(userId, giveAmount, amount);
        } catch (Exception e) {
            log.error("[user]-updateGiveAccount err! userId:{}, amount:{}, msg:{}", userId, amount, Throwables.getStackTraceAsString(e));
        }
        return result == null ? UserConstant.FALSE : UserConstant.TRUE;
    }

    @Override
    public Boolean init(String userId) {
        String autoId = userAccountInfoMapper.checkAmountIsExist(userId);
        if (StringUtils.isNotEmpty(autoId)) {
            return UserConstant.TRUE;
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
        return result == null ? UserConstant.FALSE : UserConstant.TRUE;
    }

}
