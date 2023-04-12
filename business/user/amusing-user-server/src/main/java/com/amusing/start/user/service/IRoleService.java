package com.amusing.start.user.service;

import com.amusing.start.exception.CustomException;
import com.amusing.start.user.entity.dto.RoleAddDto;
import com.amusing.start.user.entity.dto.RoleMenuBindDto;

/**
 * Created by 2023/04/11.
 *
 * @author lvqingyu
 */
public interface IRoleService {

    Integer add(String userId, RoleAddDto dto) throws CustomException;

    Boolean menuBind(String userId, RoleMenuBindDto dto) throws CustomException;

}
