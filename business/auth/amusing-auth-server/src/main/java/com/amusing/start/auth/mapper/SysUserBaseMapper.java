package com.amusing.start.auth.mapper;

import com.amusing.start.auth.pojo.SysUserBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lv.qingyu
 */
@Mapper
public interface SysUserBaseMapper {

    /**
     * 新增基础用户信息
     *
     * @param record 用户基础信息
     * @return 影响行数
     */
    int insertSelective(SysUserBase record);

    /**
     * 更新基础用户信息
     *
     * @param record 用户基础信息
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(SysUserBase record);

    /**
     * 根据手机号获取用户基础信息(有效)
     *
     * @param phone 手机号
     * @return 用户基础信息
     */
    SysUserBase selectValidByPhone(@Param("phone") String phone);

    /**
     * 根据用户名获取基础用户信息(有效)
     *
     * @param userName 用户名
     * @return 用户基础信息
     */
    SysUserBase selectValidByName(@Param("userName") String userName);

    /**
     * 根据手机号，查询未删除的用户ID
     *
     * @param phone 手机号
     * @return 用户ID
     */
    Long selectNotDelByPhone(@Param("phone") String phone);

    /**
     * 根据用户名，查询未删除的用户ID
     *
     * @param userName 用户名
     * @return 用户ID
     */
    Long selectNotDelByName(@Param("userName") String userName);

    /**
     * 根据用户ID获取基础用户信息(有效)
     *
     * @param userId 用户ID
     * @return 用户基础信息
     */
    SysUserBase selectValidByUserId(@Param("userId") String userId);

    /**
     * 根据用户名或手机号，查询未删除的用户ID
     *
     * @param userName 用户名
     * @param phone    手机号
     * @return 用户ID
     */
    Long selectNotDelByNameOrPhone(@Param("userName") String userName, @Param("phone") String phone);


}