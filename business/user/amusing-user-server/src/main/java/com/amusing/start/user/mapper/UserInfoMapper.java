package com.amusing.start.user.mapper;

import com.amusing.start.user.entity.pojo.UserInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 用户基础信息Mapper
 * @since 2022/11/26
 */
public interface UserInfoMapper {

    /**
     * @param page       分页信息
     * @param name       用户名（选填）
     * @param userIdList 用户ID（选填）
     * @return 用户基础信息列表
     * @description: 用户列表-分页
     */
    IPage<UserInfo> getList(IPage<UserInfo> page, @Param("name") String name, @Param("userIdList") List<String> userIdList);

    /**
     * @param name 用户名（必填）
     * @return 结果大于0：存在 小于或等于0：不存在
     * @description: 用户名是否存在
     */
    Integer nameExist(@Param("name") String name);

    /**
     * @param phone 手机号（必填）
     * @return 结果大于0：存在 小于或等于0：不存在
     * @description: 手机号是否存在
     */
    Integer phoneExist(@Param("phone") String phone);

    /**
     * @param name 用户名（必填）
     * @return 用户基础信息
     * @description: 根据用户名获取用户基础信息
     */
    UserInfo getByName(@Param("name") String name);

    /**
     * @param userId 用户ID（必填）
     * @param status 状态 （非必填）
     * @return 用户基础信息
     * @description: 根据用户ID与状态，获取用户基础信息
     */
    UserInfo getById(@Param("userId") String userId, @Param("status") Integer status);

    /**
     * @param userInfo 用户信息
     * @return 用户ID
     * @description: 保存用户基础信息
     */
    Integer insert(UserInfo userInfo);

    /**
     * @param userInfo 用户信息
     * @return 更新数量
     * @description: 根据ID，更新用户基础信息
     */
    Integer update(UserInfo userInfo);

}
