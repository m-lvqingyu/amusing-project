package com.amusing.start.user.service;

/**
 * @author Lv.QingYu
 * @since 2024/8/27
 */
public interface AuthService {

    /**
     * 鉴权
     *
     * @param userId  用户ID
     * @param version 版本
     * @param uri     路径
     * @return true:成功 false:失败
     */
    Boolean auth(String userId, Long version, String uri);

}
