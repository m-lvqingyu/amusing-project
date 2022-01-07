package com.amusing.start.auth.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.amusing.start.auth.dto.LoginDto;
import com.amusing.start.auth.enums.LoginType;
import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.auth.exception.code.AuthCode;
import com.amusing.start.auth.pojo.SysUserBase;
import com.amusing.start.auth.service.ILoginService;
import com.amusing.start.auth.service.IUserService;
import com.amusing.start.auth.vo.TokenVo;
import com.amusing.start.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Create By 2021/8/29
 *
 * @author lvqingyu
 */
@Slf4j
@Service
public class LoginServiceImpl implements ILoginService {

    @Resource
    private IUserService userService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public LoginServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * token 有效期(十分钟)
     */
    private final static int TOKEN_EXPIRES_TIME = 600;

    /**
     * 刷新Token 有效期(七天)
     */
    private final static int REFRESH_TOKEN_EXPIRES_TIME = 604800;


    @Override
    public TokenVo login(LoginDto loginDTO) throws AuthException {
        int loginType = loginDTO.getLoginType();
        SysUserBase userBase = null;
        // 手机号码登陆-用户身份信息校验
        if (LoginType.PHONE.getKey() == loginType) {
            userBase = doPhoneLogin(loginDTO);
        }
        // 用户名密码登陆-用户身份信息校验
        if (LoginType.USERNAME.getKey() == loginType) {
            userBase = doUsernameLogin(loginDTO);
        }
        if (userBase == null) {
            throw new AuthException(AuthCode.ERROR_AUTH);
        }
        // 返回Token信息
        return generateToken(userBase, "");
    }

    @Override
    public TokenVo refresh(String userId, String reToken, String imei) throws AuthException {
        SysUserBase sysUserBase = userService.queryByUserId(userId);
        if (sysUserBase == null) {
            log.warn("[auth]-refreshToken user not found! userId:{}", userId);
            throw new AuthException(AuthCode.USER_INFORMATION_NOT_EXIST);
        }
        String baseImei = sysUserBase.getImei();
        if (StringUtils.isNotEmpty(baseImei) && !imei.equals(baseImei)) {
            log.warn("[auth]-refreshToken checkImei fail! userId:{}, imei:{}, baseImei:{}", userId, imei, baseImei);
            throw new AuthException(AuthCode.IMEI_ERROR);
        }
        return generateToken(sysUserBase, reToken);
    }

    /**
     * 手机号码登陆
     *
     * @param loginDTO
     * @return
     */
    private SysUserBase doPhoneLogin(LoginDto loginDTO) throws AuthException {
        SysUserBase sysUserBase = userService.queryByPhone(loginDTO.getUsername());
        if (sysUserBase == null) {
            log.warn("[auth]-phoneLogin user not found! param:{}", loginDTO);
            throw new AuthException(AuthCode.ERROR_AUTH);
        }
        return sysUserBase;
    }

    /**
     * 用户名登陆
     *
     * @param loginDTO
     * @return
     */
    private SysUserBase doUsernameLogin(LoginDto loginDTO) throws AuthException {
        SysUserBase userBase = userService.queryByUserName(loginDTO.getUsername());
        if (userBase == null) {
            log.warn("[auth]-usernameLogin user not found! param:{}", loginDTO);
            throw new AuthException(AuthCode.ERROR_AUTH);
        }
        boolean matches = passwordEncoder.matches(loginDTO.getPassword(), userBase.getPassword());
        if (!matches) {
            log.warn("[auth]-usernameLogin passwords do not match! param:{}", loginDTO);
            throw new AuthException(AuthCode.ERROR_AUTH);
        }
        return userBase;
    }

    /**
     * 根据基础信息，生成Token
     *
     * @param userBase
     * @return
     */
    private TokenVo generateToken(SysUserBase userBase, String refreshToken) {
        Date currentTime = new Date();
        DateTime tokenExpiresTime = DateUtil.offsetSecond(currentTime, TOKEN_EXPIRES_TIME);
        String token = TokenUtils.generateToken(userBase.getUserId(), userBase.getSecret(), tokenExpiresTime);
        DateTime refreshTokenExpiresTime = DateUtil.offsetSecond(currentTime, REFRESH_TOKEN_EXPIRES_TIME);
        if (StringUtils.isEmpty(refreshToken)) {
            refreshToken = TokenUtils.generateToken(userBase.getUserId(), userBase.getSecret(), refreshTokenExpiresTime);
        }
        return TokenVo.builder().token(token).refreshToken(refreshToken).expiredTime(tokenExpiresTime.getTime()).build();
    }

}
