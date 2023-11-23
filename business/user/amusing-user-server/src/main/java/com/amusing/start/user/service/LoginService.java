package com.amusing.start.user.service;

import com.amusing.start.exception.CustomException;
import com.amusing.start.user.entity.dto.RegisterDto;

/**
 * @author Lv.QingYu
 * @description: 用户注册接口
 * @since 2023/11/20
 */
public interface LoginService {

    /**
     * @param registerType 注册类型 {@link com.amusing.start.user.enums.RegisterType}
     * @param dto          用户信息
     * @return true:成功 false:失败
     * @throws CustomException 异常信息
     * @description: 用户注册
     */
    Boolean register(Integer registerType, RegisterDto dto) throws CustomException;


}
