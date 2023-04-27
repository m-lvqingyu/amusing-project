package com.amusing.start.user.service;

import com.amusing.start.exception.CustomException;
import com.amusing.start.user.entity.dto.LoginDto;
import com.amusing.start.user.entity.dto.RegisterDto;
import com.amusing.start.user.entity.vo.TokenVo;

/**
 * Created by 2023/04/12.
 *
 * @author lvqingyu
 */
public interface ILoginService {

    /**
     * 用户-注册校验
     *
     * @param name  用户名
     * @param phone 手机号
     * @return true:通过 false:失败
     */
    Boolean check(String name, String phone) throws CustomException;

    /**
     * 用户-注册
     *
     * @param dto 用户信息
     * @return true:成功 false:失败
     * @throws CustomException
     */
    Boolean register(RegisterDto dto) throws CustomException;

    /**
     * 用户-登陆
     *
     * @param dto 用户信息
     * @return token
     */
    TokenVo login(LoginDto dto) throws CustomException;

    /**
     * 用户-token刷新
     *
     * @param token token
     * @return token信息
     */
    TokenVo refresh(String token) throws CustomException;

}
