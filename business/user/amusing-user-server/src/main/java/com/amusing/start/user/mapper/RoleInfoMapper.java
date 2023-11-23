package com.amusing.start.user.mapper;

import com.amusing.start.user.entity.pojo.RoleInfo;
import com.amusing.start.user.entity.vo.RoleInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleInfoMapper {

    List<RoleInfoVo> selectByNameOrCode(IPage<RoleInfoVo> page, @Param("name") String name, @Param("code") String code);

    List<RoleInfo> selectAll();

    List<RoleInfo> selectValidAll();

    RoleInfo selectById(@Param("id") Integer id);

    RoleInfo selectByName(@Param("name") String name);

    RoleInfo selectByCode(@Param("code") String code);


    Integer update(RoleInfo roleInfo);

    Integer insert(RoleInfo roleInfo);

    Integer getAdminRoleId();

    List<RoleInfo> getByIdList(@Param("idList") List<Integer> idList);


}
