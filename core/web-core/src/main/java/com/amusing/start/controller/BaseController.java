package com.amusing.start.controller;

import com.amusing.start.code.CommCode;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.constant.AuthConstant;
import com.amusing.start.exception.CustomException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Create By 2021/8/21
 *
 * @author lvqingyu
 */
public class BaseController {

    private HttpServletRequest request;

    public BaseController(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 获取用户唯一Id
     *
     * @return 用户唯一ID
     */
    public String getUserId() throws CustomException {
        return Optional.ofNullable(request).map((i) -> i.getHeader(AuthConstant.USER_UID)).orElseThrow(() -> new CustomException(CommCode.UNAUTHORIZED));
    }

    /**
     * 获取设备号
     *
     * @return 设备号
     */
    public String getImei() {
        return Optional.ofNullable(request).map((i) -> i.getHeader(AuthConstant.IMEI)).orElseGet(() -> "");
    }

}