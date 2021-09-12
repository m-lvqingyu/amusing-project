package com.amusing.start.auth.service;

import com.amusing.start.auth.dto.LoginDTO;
import com.amusing.start.result.ApiResult;

/**
 * Create By 2021/8/29
 *
 * @author lvqingyu
 */
public interface LoginService {

    /**
     * 用户登录
     *
     * @param loginDTO
     * @return
     */
    ApiResult login(LoginDTO loginDTO);

}
