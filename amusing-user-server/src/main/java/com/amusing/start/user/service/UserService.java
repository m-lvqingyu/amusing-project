package com.amusing.start.user.service;

import com.amusing.start.user.entity.pojo.UserInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2022/11/26
 */
public interface UserService {

    /**
     * 员工列表（分页）
     *
     * @param name    姓名
     * @param phone   手机号
     * @param sources 来源
     * @param status  状态
     * @param page    页码
     * @param size    每页行数
     * @return 员工信息
     */
    Page<UserInfo> page(String name, String phone, Integer sources, Integer status, Integer page, Integer size);

    /**
     * 根据ID获取用户信息
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 用户信息
     */
    UserInfo getById(String userId, Integer status);

    /**
     * 根据ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfo getById(String userId);

    /**
     * 根据用户名获取用户信息
     *
     * @param name   用户名
     * @param status 状态
     * @return 用户信息
     */
    UserInfo getByName(String name, Integer status);

    /**
     * 根据用户名获取用户信息
     *
     * @param name 用户名
     * @return 用户信息
     */
    UserInfo getByName(String name);

    /**
     * 根据手机号获取用户信息
     *
     * @param phone  手机号
     * @param status 状态
     * @return 用户信息
     */
    UserInfo getByPhone(String phone, Integer status);

    /**
     * 根据手机号获取用户信息
     *
     * @param phone 手机号
     * @return 用户信息
     */
    UserInfo getByPhone(String phone);

    /**
     * 保存用户
     *
     * @param userInfo 用户信息
     * @return 保存结果
     */
    Integer insert(UserInfo userInfo);

    /**
     * 更新版本号
     *
     * @param userId      用户ID
     * @param origVersion 原版本号
     * @param newVersion  新版本号
     * @return 更新结果
     */
    Integer updateVersion(String userId, Long origVersion, Long newVersion);

    /**
     * 修改员工密码
     *
     * @param userId        员工ID
     * @param operateUserId 操作人ID
     * @param password      密码
     * @return true:成功 false:失败
     */
    Boolean changePw(String userId, String operateUserId, String password);

    /**
     * 修改员工信息
     *
     * @param userInfo 员工信息
     * @return true:成功 false:失败
     */
    Boolean update(UserInfo userInfo);


}
