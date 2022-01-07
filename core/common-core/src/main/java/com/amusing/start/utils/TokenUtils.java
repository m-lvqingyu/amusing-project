package com.amusing.start.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
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
     * @param userUid     用户唯一ID
     * @param secret      用户密钥
     * @param expiresTime 过期时间
     * @return
     */
    public static String generateToken(String userUid, String secret, Date expiresTime) {
        return JWT.create()
                .withAudience(userUid)
                .withExpiresAt(expiresTime)
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * 获得用户唯一Id
     *
     * @param token token
     * @return
     */
    public static String getUserId(String token) {
        DecodedJWT decode = JWT.decode(token);
        // Token过期时间校验
        long expiresTime = decode.getExpiresAt().getTime();
        if (expiresTime <= System.currentTimeMillis()) {
            return null;
        }
        // 获取用户唯一Id
        List<String> audience = JWT.decode(token).getAudience();
        if (CollectionUtil.isEmpty(audience)) {
            return null;
        }
        return audience.get(0);
    }

}
