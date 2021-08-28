package com.amusing.start.auth.service.impl;

import com.amusing.start.auth.dto.LoginDTO;
import com.amusing.start.auth.enums.LoginType;
import com.amusing.start.auth.enums.UserDel;
import com.amusing.start.auth.enums.UserStatus;
import com.amusing.start.auth.mapper.SysUserBaseMapper;
import com.amusing.start.auth.pojo.SysUserBase;
import com.amusing.start.auth.pojo.SysUserBaseExample;
import com.amusing.start.auth.service.UserBaseService;
import com.amusing.start.auth.utils.TokenUtils;
import com.amusing.start.result.ApiCode;
import com.amusing.start.result.ApiResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
@Service
public class UserBaseServiceImpl implements UserBaseService {

    @Resource
    private SysUserBaseMapper sysUserBaseMapper;

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
        SysUserBaseExample example = new SysUserBaseExample();
        example.createCriteria().andPhoneEqualTo(loginDTO.getUsername())
                .andSourcesEqualTo(loginDTO.getUserType())
                .andStatusEqualTo(UserStatus.VALID.getKey())
                .andIsDelEqualTo(UserDel.NOT_DELETED.getKey());
        return generateToken(example);
    }

    private ApiResult usernameLogin(LoginDTO loginDTO) {
        SysUserBaseExample example = new SysUserBaseExample();
        example.createCriteria().andUserNameEqualTo(loginDTO.getUsername())
                .andPasswordEqualTo(loginDTO.getPassword())
                .andSourcesEqualTo(loginDTO.getUserType())
                .andStatusEqualTo(UserStatus.VALID.getKey())
                .andIsDelEqualTo(UserDel.NOT_DELETED.getKey());
        return generateToken(example);
    }

    private ApiResult generateToken(SysUserBaseExample example) {
        List<SysUserBase> baseList = sysUserBaseMapper.selectByExample(example);
        if (baseList == null || baseList.isEmpty()) {
            return ApiResult.fail(ApiCode.ERROR_AUTH);
        }
        SysUserBase sysUserBase = baseList.get(0);
        String token = TokenUtils.generateToken(sysUserBase.getUserId(), sysUserBase.getSecret());
        return ApiResult.ok(token);
    }
}
