package com.amusing.start.user.service;

import com.amusing.start.user.entity.vo.MenuVo;

import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 角色管理Service
 * @since 2021/09/21
 */
public interface IMenuService {

    List<MenuVo> getMenuTree(String userId);

}
