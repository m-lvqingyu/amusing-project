package com.amusing.start.auth.service;

import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.auth.pojo.SysUserBase;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
public interface IUserService {

    /**
     * 根据手机号码，获取用户信息
     *
     * @param phone 手机号
     * @return 用户信息
     * @throws AuthException
     */
    SysUserBase queryByPhone(String phone) throws AuthException;

    /**
     * 根据用户名，获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     * @throws AuthException
     */
    SysUserBase queryByUserName(String username) throws AuthException;

    /**
     * 根据ID，获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     * @throws AuthException
     */
    SysUserBase queryByUserId(String userId) throws AuthException;

    /**
     * 根据用户名或手机号，查询未删除的用户ID
     *
     * @param userName 用户名
     * @param phone    手机号
     * @return 用户ID
     * @throws AuthException
     */
    String queryNotDelByNameOrPhone(String userName, String phone) throws AuthException;

    /**
     * 保存基础用户
     *
     * @param userBase 用户基础信息
     * @return
     * @throws AuthException
     */
    Integer saveUser(SysUserBase userBase) throws AuthException;

    /**
     * 判断用户是否有效
     *
     * @param userId 用户ID
     * @return true:有效 false:无效
     * @throws AuthException
     */
    Boolean checkUserValid(String userId) throws AuthException;

}
