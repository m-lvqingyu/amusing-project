package com.amusing.start.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

/**
 * Create By 2021/8/21
 *
 * @author lvqingyu
 */
public class TokenUtils {

    public static final String ROLE_LIST_KEY = "r_id";

    public static final String USER_ID_KEY = "u_id";

    public static final String IS_ADMIN = "admin";

    /**
     * 生成Token
     *
     * @param userUid     用户唯一ID
     * @param roleIds     角色集合
     * @param secret      用户密钥
     * @param expiresTime 过期时间
     * @return
     */
    public static String generateToken(String userUid, Integer[] roleIds, Boolean isAdmin, String secret, Date expiresTime) {
        return JWT.create().withClaim(USER_ID_KEY, userUid).withArrayClaim(ROLE_LIST_KEY, roleIds)
                .withClaim(IS_ADMIN, isAdmin)
                .withExpiresAt(expiresTime).sign(Algorithm.HMAC256(secret));
    }

    /**
     * 获得用户唯一Id
     *
     * @param token token
     * @return
     */
    public static Map<String, Claim> getClaims(String token) {
        DecodedJWT decode = JWT.decode(token);
        // Token过期时间校验
        long expiresTime = decode.getExpiresAt().getTime();
        if (expiresTime <= System.currentTimeMillis()) {
            return null;
        }
        return JWT.decode(token).getClaims();
    }

    public static String getUserId(Map<String, Claim> claimMap) {
        if (CollectionUtil.isEmpty(claimMap)) {
            return null;
        }
        Claim claim = claimMap.get(TokenUtils.USER_ID_KEY);
        if (claim == null) {
            return null;
        }
        return claim.asString();
    }

    public static Integer[] getRoleIds(Map<String, Claim> claimMap) {
        if (CollectionUtil.isEmpty(claimMap)) {
            return null;
        }
        Claim claim = claimMap.get(TokenUtils.ROLE_LIST_KEY);
        return claim.asArray(Integer.class);
    }

    public static Boolean getAdmin(Map<String, Claim> claimMap) {
        if (CollectionUtil.isEmpty(claimMap)) {
            return null;
        }
        Claim claim = claimMap.get(TokenUtils.IS_ADMIN);
        return claim.asBoolean();
    }

}
