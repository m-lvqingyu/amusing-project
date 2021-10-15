package com.amusing.start.auth.service;

import com.amusing.start.auth.dto.UserCreateDto;
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
     * @param executorUserId
     * @param createDTO
     * @return
     */
    ApiResult create(String executorUserId, UserCreateDto createDTO) throws AuthException;

}
