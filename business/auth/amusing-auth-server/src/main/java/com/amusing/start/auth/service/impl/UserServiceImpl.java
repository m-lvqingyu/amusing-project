package com.amusing.start.auth.service.impl;

import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.auth.mapper.SysUserBaseMapper;
import com.amusing.start.auth.pojo.SysUserBase;
import com.amusing.start.auth.service.IUserService;
import com.amusing.start.code.CommCode;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
            return sysUserBaseMapper.selectNotDelByPhone(phone);
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

}
