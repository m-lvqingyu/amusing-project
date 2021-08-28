package com.amusing.start.auth.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.List;

/**
 * Create By 2021/8/21
 *
 * @author lvqingyu
 */
public class TokenUtils {

    /**
     * 生成Token
     *
     * @param userUid 用户唯一ID
     * @param secret  用户密钥
     * @return
     */
    public static String generateToken(String userUid, String secret) {
        return JWT.create().withAudience(userUid).sign(Algorithm.HMAC256(secret));
    }

    public static JWTVerifier buildVerifier(String secret) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        return verifier;
    }

    /**
     * 获得用户唯一Id
     *
     * @param token
     * @return
     */
    public static String getUserId(String token) {
        List<String> audience = JWT.decode(token).getAudience();
        if (audience == null || audience.isEmpty()) {
            return null;
        }
        return audience.get(0);
    }

}
