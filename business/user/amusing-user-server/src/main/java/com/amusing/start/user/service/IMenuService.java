package com.amusing.start.user.service;

import com.amusing.start.exception.CustomException;
import com.amusing.start.user.entity.vo.MenuVo;

import java.util.List;

/**
 * Created by 2023/2/15.
 *
 * @author lvqingyu
 */
public interface IMenuService {

    Boolean matchPath(String userId, String path) throws CustomException;

    List<MenuVo> getMenuTree(String userId);

}
