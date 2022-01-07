package com.amusing.start.auth.service;

import com.amusing.start.auth.dto.LoginDto;
import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.auth.vo.TokenVo;

/**
 * Create By 2021/8/29
 *
 * @author lvqingyu
 */
public interface ILoginService {

    /**
     * 用户登录
     *
     * @param loginDTO 用户登陆信息
     * @return Token信息
     */
    TokenVo login(LoginDto loginDTO) throws AuthException;

    /**
     * Token -刷新
     *
     * @param userId  用户ID
     * @param reToken 刷新Token
     * @param imei    设备编号
     * @return Token信息
     * @throws AuthException
     */
    TokenVo refresh(String userId, String reToken, String imei) throws AuthException;
}
