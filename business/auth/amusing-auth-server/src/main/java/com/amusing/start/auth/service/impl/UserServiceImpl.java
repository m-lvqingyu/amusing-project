package com.amusing.start.auth.service.impl;

import com.amusing.start.auth.enums.UserStatus;
import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.auth.exception.code.AuthCode;
import com.amusing.start.auth.mapper.SysUserBaseMapper;
import com.amusing.start.auth.pojo.SysUserBase;
import com.amusing.start.auth.service.IUserService;
import com.amusing.start.code.CommCode;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private SysUserBaseMapper sysUserBaseMapper;

    @Override
    public SysUserBase queryByPhone(String phone) throws AuthException {
        try {
            return sysUserBaseMapper.selectValidByPhone(phone);
        } catch (Exception e) {
            log.error("[auth]-queryByPhone err! param:{}, mag:{}", phone, Throwables.getStackTraceAsString(e));
            throw new AuthException(CommCode.FAIL);
        }
    }

    @Override
    public SysUserBase queryByUserName(String username) throws AuthException {
        try {
            return sysUserBaseMapper.selectValidByName(username);
        } catch (Exception e) {
            log.error("[auth]-queryByUserName err! param:{}, mag:{}", username, Throwables.getStackTraceAsString(e));
            throw new AuthException(CommCode.FAIL);
        }
    }

    @Override
    public SysUserBase queryByUserId(String userId) throws AuthException {
        try {
            return sysUserBaseMapper.selectValidByUserId(userId);
        } catch (Exception e) {
            log.error("[auth]-queryByUserName err! param:{}, mag:{}", userId, Throwables.getStackTraceAsString(e));
            throw new AuthException(CommCode.FAIL);
        }
    }

    @Override
    public Long queryNotDelByNameOrPhone(String userName, String phone) throws AuthException {
        try {
            return sysUserBaseMapper.selectNotDelByNameOrPhone(userName, phone);
        } catch (Exception e) {
            log.error("[auth]-queryNotDelByNameOrPhone err! userName:{}, phone:{}, mag:{}", userName, phone, Throwables.getStackTraceAsString(e));
            throw new AuthException(CommCode.FAIL);
        }
    }

    @Override
    public Integer saveUser(SysUserBase userBase) throws AuthException {
        try {
            return sysUserBaseMapper.insertSelective(userBase);
        } catch (Exception e) {
            log.error("[auth]-saveUser err! param:{}, mag:{}", userBase, Throwables.getStackTraceAsString(e));
            throw new AuthException(CommCode.FAIL);
        }
    }

    @Override
    public Boolean checkUserValid(String userId) throws AuthException {
        Integer status;
        try {
            status = sysUserBaseMapper.selectStatusByUserId(userId);
        } catch (Exception e) {
            log.error("[auth]-checkUserValid err! userId:{}, msg:{}", userId, Throwables.getStackTraceAsString(e));
            throw new AuthException(CommCode.FAIL);
        }
        log.info("[auth]-checkUserValid userId:{}, status:{}", userId, status);
        Optional.ofNullable(status).orElseThrow(() -> new AuthException(AuthCode.USER_INFORMATION_NOT_EXIST));
        if (UserStatus.VALID.getKey() == status) {
            return true;
        }
        if (UserStatus.INVALID.getKey() == status) {
            return false;
        }
        if (UserStatus.FROZEN.getKey() == status) {
            throw new AuthException(AuthCode.USER_FROZEN_ERR);
        }
        throw new AuthException(AuthCode.USER_INFORMATION_NOT_EXIST);
    }

}
