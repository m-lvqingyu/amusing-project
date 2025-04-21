package com.amusing.start.controller;

import com.amusing.start.code.CommunalCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Create By 2021/8/21
 *
 * @author lvqingyu
 */
public class BaseController {

    private final HttpServletRequest request;

    public BaseController(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 获取用户唯一Id
     *
     * @return 用户唯一ID
     */
    public String getUserId() throws CustomException {
        String userId = request.getHeader(CommConstant.USER_UID);
        if (StringUtils.isNotBlank(userId)) {
            return userId;
        }
        throw new CustomException(CommunalCode.UNAUTHORIZED);
    }

}