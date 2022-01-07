package com.amusing.start.controller;

import com.amusing.start.constant.CommonConstant;

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
    public String getUserId() {
        return Optional.ofNullable(request).map((i) -> i.getHeader(CommonConstant.USER_UID)).orElseGet(() -> "");
    }

    /**
     * 获取设备号
     *
     * @return 设备号
     */
    public String getImei() {
        return Optional.ofNullable(request).map((i) -> i.getHeader(CommonConstant.IMEI)).orElseGet(() -> "");
    }

}