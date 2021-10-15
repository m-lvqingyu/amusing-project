package com.amusing.start.controller;

import com.amusing.start.constant.CommonConstant;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

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
     * @return
     */
    public String getUserId() {
        if (request == null) {
            return "";
        }
        String userUid = request.getHeader(CommonConstant.USER_UID_HEADER_KEY);
        if (StringUtils.isEmpty(userUid)) {
            return "";
        }
        return userUid;
    }

}