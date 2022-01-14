package com.amusing.start.auth.service;

import com.amusing.start.auth.dto.UserRegisterDto;
import com.amusing.start.auth.exception.AuthException;

/**
 * @author lv.qingyu
 */
public interface IRegisterService {

    /**
     * 创建基础用户-普通用户
     *
     * @param registerDto 用户信息
     * @return 用户ID
     * @throws AuthException 异常
     */
    String userRegister(UserRegisterDto registerDto) throws AuthException;

}
