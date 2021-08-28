package com.amusing.start.auth.service;

import com.amusing.start.auth.dto.LoginDTO;
import com.amusing.start.result.ApiResult;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
public interface UserBaseService {

    /**
     * 用户登录
     *
     * @param loginDTO
     * @return
     */
    ApiResult login(LoginDTO loginDTO);
}
