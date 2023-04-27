package com.amusing.start.user.mapper;

import com.amusing.start.user.entity.pojo.UserInfo;
import com.amusing.start.user.entity.vo.user.UserListVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
 * Created by 2023/2/15.
 *
 * @author lvqingyu
 */
public interface UserInfoMapper {

    /**
     * 分页查询用户列表
     *
     * @param page  分页参数
     * @param name  用户名
     * @param phone 手机号
     * @return 用户列表
     */
    IPage<UserListVo> selectList(IPage<UserListVo> page, @Param("name") String name, @Param("phone") String phone);

    /**
     * 判断用户名或者手机号是否存在
     *
     * @param name  用户名
     * @param phone 手机号
     * @return 用户信息
     */
    UserInfo selectByNameOrPhone(@Param("name") String name, @Param("phone") String phone);

    /**
     * 根据用户唯一ID，获取用户信息
     *
     * @param userId 用户唯一ID
     * @return 用户信息
     */
    UserInfo selectById(@Param("userId") String userId);

    /**
     * 新增用户信息
     *
     * @param userInfo 用户信息
     * @return 影响条数
     */
    Integer insert(UserInfo userInfo);

    /**
     * 更新用户信息
     *
     * @param userInfo 用户信息
     * @return 影响条数
     */
    Integer update(UserInfo userInfo);

}
