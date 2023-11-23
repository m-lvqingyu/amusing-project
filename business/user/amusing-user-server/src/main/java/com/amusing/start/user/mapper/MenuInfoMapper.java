package com.amusing.start.user.mapper;

import com.amusing.start.user.entity.pojo.MenuInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuInfoMapper {

    List<MenuInfo> selectAll();

    List<MenuInfo> selectValidAll();

    MenuInfo selectById(@Param("id") Integer id);

    List<MenuInfo> selectByIds(@Param("ids") List<Integer> ids);

    int insert(MenuInfo menuInfo);

    int update(MenuInfo menuInfo);

}
