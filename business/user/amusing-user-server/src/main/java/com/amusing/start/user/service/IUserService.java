package com.amusing.start.user.service;

import com.amusing.start.exception.CustomException;
import com.amusing.start.user.entity.dto.LoginDto;
import com.amusing.start.user.entity.dto.RegisterDto;
import com.amusing.start.user.entity.vo.TokenVo;

/**
 * Created by lvqingyu on 2022/10/2.
 */
public interface IUserService {

    /**
     * 注册校验
     *
     * @param type  {@link com.amusing.start.user.enums.RegisterPreType}
     * @param param 用户名或者手机号
     * @return true:通过 false:失败
     */
    Boolean registerPre(Integer type, String param);

    /**
     * 注册
     *
     * @param dto 用户信息
     * @return true:成功 false:失败
     * @throws CustomException
     */
    Boolean register(RegisterDto dto) throws CustomException;

    /**
     * 登陆
     *
     * @param dto 用户信息
     * @return token
     */
    TokenVo login(LoginDto dto) throws CustomException;

    /**
     * token刷新
     *
     * @param token token
     * @return
     */
    TokenVo refresh(String token) throws CustomException;

}
