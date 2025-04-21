package com.amusing.start.utils;

import com.amusing.start.code.CommunalCode;
import com.amusing.start.response.UserLoginResponse;
import com.amusing.start.exception.CustomException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * Create By 2021/8/21
 *
 * @author lvqingyu
 */
public class TokenUtils {

    public static final String ID_NAME = "uid";

    public static final String VERSION = "vs";

    /**
     * 生成Token
     *
     * @param userId      用户唯一ID
     * @param secret      用户密钥
     * @param version     登录版本号
     * @param expiresTime 过期时间
     * @return Token
     */
    public static String generateToken(String userId, String secret, Long version, Date expiresTime) {
        return JWT.create().withClaim(ID_NAME, userId).withClaim(VERSION, version)
                .withExpiresAt(expiresTime).sign(Algorithm.HMAC256(secret));
    }

    /**
     * Token解析
     *
     * @param token Token
     * @return 用户登录信息
     */
    public static UserLoginResponse parse(String token) {
        DecodedJWT decode = JWT.decode(token);
        if (decode.getExpiresAt().getTime() < System.currentTimeMillis()) {
            throw new CustomException(CommunalCode.TOKEN_EXPIRES);
        }
        Map<String, Claim> claims = JWT.decode(token).getClaims();
        String userId = claims.get(ID_NAME).asString();
        if (StringUtils.isBlank(userId)) {
            throw new CustomException(CommunalCode.UNAUTHORIZED);
        }
        Long version = claims.get(VERSION).asLong();
        if (version == null) {
            throw new CustomException(CommunalCode.UNAUTHORIZED);
        }
        return new UserLoginResponse().setUserId(userId).setVersion(version);
    }

}
