package com.amusing.start.auth.service.impl;

import com.amusing.start.auth.dto.LoginDto;
import com.amusing.start.auth.enums.LoginType;
import com.amusing.start.auth.exception.code.AuthCode;
import com.amusing.start.auth.mapper.SysUserBaseMapper;
import com.amusing.start.auth.pojo.SysUserBase;
import com.amusing.start.auth.service.LoginService;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import com.amusing.start.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Create By 2021/8/29
 *
 * @author lvqingyu
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private SysUserBaseMapper sysUserBaseMapper;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public LoginServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ApiResult login(LoginDto loginDTO) {
        Integer loginType = loginDTO.getLoginType();
        if (LoginType.PHONE.getKey() == loginType) {
            return phoneLogin(loginDTO);
        }
        if (LoginType.USERNAME.getKey() == loginType) {
            return usernameLogin(loginDTO);
        }
        return ApiResult.fail(CommCode.SYSTEM_LOGIN_EXCEPTION);
    }

    /**
     * 手机号码登陆
     *
     * @param loginDTO
     * @return
     */
    private ApiResult phoneLogin(LoginDto loginDTO) {
        SysUserBase sysUserBase = sysUserBaseMapper.selectValidByPhone(loginDTO.getUsername());
        if (sysUserBase == null) {
            return ApiResult.fail(AuthCode.ERROR_AUTH);
        }
        return generateToken(sysUserBase);
    }

    /**
     * 用户名登陆
     *
     * @param loginDTO
     * @return
     */
    private ApiResult usernameLogin(LoginDto loginDTO) {
        SysUserBase userBase = sysUserBaseMapper.selectValidByName(loginDTO.getUsername());
        if (userBase == null) {
            return ApiResult.fail(AuthCode.ERROR_AUTH);
        }
        String dtoPassword = loginDTO.getPassword();
        String password = userBase.getPassword();
        boolean matches = passwordEncoder.matches(dtoPassword, password);
        if (!matches) {
            return ApiResult.fail(AuthCode.ERROR_AUTH);
        }
        return generateToken(userBase);
    }

    /**
     * 根据基础信息，生成Token
     *
     * @param userBase
     * @return
     */
    private ApiResult generateToken(SysUserBase userBase) {
        String token = TokenUtils.generateToken(userBase.getUserId(), userBase.getSecret());
        return ApiResult.ok(token);
    }

}
