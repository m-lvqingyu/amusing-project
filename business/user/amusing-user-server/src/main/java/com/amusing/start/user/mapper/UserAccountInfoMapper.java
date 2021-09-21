package com.amusing.start.user.mapper;

import com.amusing.start.user.pojo.UserAccountInfo;
import com.amusing.start.user.pojo.UserAccountInfoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lv.qingyu
 */
@Mapper
public interface UserAccountInfoMapper {
    long countByExample(UserAccountInfoExample example);

    int deleteByExample(UserAccountInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserAccountInfo record);

    int insertSelective(UserAccountInfo record);

    List<UserAccountInfo> selectByExample(UserAccountInfoExample example);

    UserAccountInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserAccountInfo record, @Param("example") UserAccountInfoExample example);

    int updateByExample(@Param("record") UserAccountInfo record, @Param("example") UserAccountInfoExample example);

    int updateByPrimaryKeySelective(UserAccountInfo record);

    int updateByPrimaryKey(UserAccountInfo record);
}