package com.amusing.start.auth.mapper;

import com.amusing.start.auth.pojo.SysUserBase;
import com.amusing.start.auth.pojo.SysUserBaseExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lv.qingyu
 */
@Mapper
public interface SysUserBaseMapper {
    long countByExample(SysUserBaseExample example);

    int deleteByExample(SysUserBaseExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SysUserBase record);

    int insertSelective(SysUserBase record);

    List<SysUserBase> selectByExample(SysUserBaseExample example);

    SysUserBase selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SysUserBase record, @Param("example") SysUserBaseExample example);

    int updateByExample(@Param("record") SysUserBase record, @Param("example") SysUserBaseExample example);

    int updateByPrimaryKeySelective(SysUserBase record);

    int updateByPrimaryKey(SysUserBase record);
}