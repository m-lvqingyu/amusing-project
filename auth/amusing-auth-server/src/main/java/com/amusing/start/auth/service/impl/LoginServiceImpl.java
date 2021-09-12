package com.amusing.start.auth.service.impl;

import com.amusing.start.auth.dto.LoginDTO;
import com.amusing.start.auth.enums.LoginType;
import com.amusing.start.auth.enums.UserDel;
import com.amusing.start.auth.enums.UserStatus;
import com.amusing.start.auth.mapper.SysUserBaseMapper;
import com.amusing.start.auth.pojo.SysUserBase;
import com.amusing.start.auth.pojo.SysUserBaseExample;
import com.amusing.start.auth.service.LoginService;
import com.amusing.start.result.ApiCode;
import com.amusing.start.result.ApiResult;
import com.amusing.start.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    public ApiResult login(LoginDTO loginDTO) {
        Integer loginType = loginDTO.getLoginType();
        if (LoginType.PHONE.getKey() == loginType) {
            return phoneLogin(loginDTO);
        }
        if (LoginType.USERNAME.getKey() == loginType) {
            return usernameLogin(loginDTO);
        }
        return ApiResult.fail(ApiCode.SYSTEM_LOGIN_EXCEPTION);
    }

    private ApiResult phoneLogin(LoginDTO loginDTO) {
        SysUserBase sysUserBase = userBase(loginDTO);
        if (sysUserBase == null) {
            return ApiResult.fail(ApiCode.ERROR_AUTH);
        }
        return generateToken(sysUserBase);
    }

    private ApiResult usernameLogin(LoginDTO loginDTO) {
        SysUserBase userBase = userBase(loginDTO);
        if (userBase == null) {
            return ApiResult.fail(ApiCode.ERROR_AUTH);
        }
        String dtoPassword = loginDTO.getPassword();
        String password = userBase.getPassword();
        boolean matches = passwordEncoder.matches(dtoPassword, password);
        if (!matches) {
            return ApiResult.fail(ApiCode.ERROR_AUTH);
        }
        return generateToken(userBase);
    }

    private SysUserBase userBase(LoginDTO loginDTO) {
        Integer loginType = loginDTO.getLoginType();
        SysUserBaseExample example = new SysUserBaseExample();
        SysUserBaseExample.Criteria criteria = example.createCriteria();
        if (LoginType.PHONE.getKey() == loginType) {
            criteria.andPhoneEqualTo(loginDTO.getUsername());
        }
        if (LoginType.USERNAME.getKey() == loginType) {
            criteria.andUserNameEqualTo(loginDTO.getUsername());
        }
        criteria.andSourcesEqualTo(loginDTO.getUserType());
        criteria.andStatusEqualTo(UserStatus.VALID.getKey());
        criteria.andIsDelEqualTo(UserDel.NOT_DELETED.getKey());
        List<SysUserBase> baseList = sysUserBaseMapper.selectByExample(example);
        if (baseList == null || baseList.isEmpty()) {
            return null;
        }
        SysUserBase sysUserBase = baseList.get(0);
        return sysUserBase;
    }

    private ApiResult generateToken(SysUserBase userBase) {
        String token = TokenUtils.generateToken(userBase.getUserId(), userBase.getSecret());
        return ApiResult.ok(token);
    }
}
