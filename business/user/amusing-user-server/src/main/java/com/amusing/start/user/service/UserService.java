package com.amusing.start.user.service;

import com.amusing.start.user.entity.pojo.UserInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 用户管理Service
 * @since 2022/11/26
 */
public interface UserService {

    /**
     * @param name       用户名
     * @param userIdList 用户ID
     * @param page       页码
     * @param size       每页条目
     * @return 用户列表
     * @description: 用户列表-分页
     */
    Page<UserInfo> getPage(String name, List<String> userIdList, Integer page, Integer size);

    /**
     * @param userId 用户ID
     * @param status 状态 {@link com.amusing.start.user.enums.UserStatus}
     * @return 基础信息
     * @description: 根据用户ID获取用户基础信息
     */
    UserInfo getById(String userId, Integer status);

    /**
     * @param name 用户名
     * @return 基础信息
     * @Description: 根据用户名获取用户基础信息
     */
    UserInfo getByName(String name);

    /**
     * @param name 用户名
     * @return true:存在 false:不存在
     * @description: 判断用户名是否已存在
     */
    Integer nameExist(String name);

    /**
     * @param phone 手机号
     * @return true:存在 false:不存在
     * @description: 判断手机号是否已存在
     */
    Integer phoneExist(String phone);

    /**
     * @param userInfo 用户信息
     * @return 大于0：成功 小于或等于0：失败
     * @description: 更新用户信息
     */
    Integer update(UserInfo userInfo);

    /**
     * @param userInfo 用户信息
     * @return 用户ID
     * @description: 保存用户信息
     */
    Integer insert(UserInfo userInfo);

}
