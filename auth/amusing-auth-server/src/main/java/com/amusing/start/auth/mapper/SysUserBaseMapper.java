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
     * @param record
     * @return
     */
    int insertSelective(SysUserBase record);

    /**
     * 更新基础用户信息
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SysUserBase record);

    /**
     * 根据手机号获取用户基础信息(有效)
     *
     * @param phone
     * @return
     */
    SysUserBase selectValidByPhone(@Param("phone") String phone);

    /**
     * 根据用户名获取基础用户信息(有效)
     *
     * @param userName
     * @return
     */
    SysUserBase selectValidByName(@Param("userName") String userName);

    /**
     * 根据手机号，查询未删除的用户ID
     *
     * @param phone
     * @return
     */
    Long selectNotDelByPhone(@Param("phone") String phone);

    /**
     * 根据用户名，查询未删除的用户ID
     *
     * @param userName
     * @return
     */
    Long selectNotDelByName(@Param("userName") String userName);

}