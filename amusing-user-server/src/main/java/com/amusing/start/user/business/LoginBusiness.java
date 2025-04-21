package com.amusing.start.user.business;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.response.UserLoginResponse;
import com.amusing.start.user.entity.pojo.UserInfo;
import com.amusing.start.user.entity.request.login.LoginRequest;
import com.amusing.start.user.entity.response.TokenResponse;
import com.amusing.start.user.enums.UserErrorCode;
import com.amusing.start.user.enums.UserStatus;
import com.amusing.start.user.service.UserService;
import com.amusing.start.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class LoginBusiness {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    /**
     * token过期时间
     */
    public final static int TOKEN_EXPIRES_TIME = 72000;

    /**
     * refreshToken过期时间
     */
    public final static int REFRESH_TOKEN_EXPIRES_TIME = 604800;

    public TokenResponse login(LoginRequest request) {
        UserInfo user = userService.getByName(request.getUserName(), UserStatus.VALID.getKey());
        if (user == null) throw new CustomException(UserErrorCode.USER_NOT_FOUND);
        String password = request.getPassword();
        String userPassword = user.getPassword();
        if (!passwordEncoder.matches(password, userPassword)) throw new CustomException(UserErrorCode.PASSWORD_ERR);
        addLoginVersion(user);
        String id = user.getId();
        String secret = user.getSecret();
        Long version = user.getVersion();
        String token = generateToken(id, version, secret, TOKEN_EXPIRES_TIME);
        String refreshToken = generateToken(id, version, secret, REFRESH_TOKEN_EXPIRES_TIME);
        return new TokenResponse().setToken(token).setRefreshToken(refreshToken);
    }

    public String refresh(String refreshToken) {
        UserLoginResponse loginDto = TokenUtils.parse(refreshToken);
        UserInfo userInfo = userService.getById(loginDto.getUserId(), UserStatus.VALID.getKey());
        addLoginVersion(userInfo);
        return generateToken(userInfo.getId(), userInfo.getVersion(), userInfo.getSecret(), TOKEN_EXPIRES_TIME);
    }


    private void addLoginVersion(UserInfo userInfo) {
        Long version = userInfo.getVersion();
        int update = userService.updateVersion(userInfo.getId(), version, version + CommConstant.ONE);
        if (update <= CommConstant.ZERO) {
            throw new CustomException(CommunalCode.REQUEST_LIMIT_ERR);
        }
    }

    private String generateToken(String userId, Long version, String secret, Integer expiresTime) {
        Date currentTime = new Date();
        DateTime tokenExpiresTime = DateUtil.offsetSecond(currentTime, expiresTime);
        return TokenUtils.generateToken(userId, secret, version, tokenExpiresTime);
    }

}
