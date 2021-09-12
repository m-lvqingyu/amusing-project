package com.amusing.start.auth.service;

import com.amusing.start.auth.dto.UserCreateDTO;
import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.result.ApiResult;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
public interface UserService {

    /**
     * 创建基础用户
     *
     * @param createDTO
     * @return
     */
    ApiResult create(UserCreateDTO createDTO) throws AuthException;

}
