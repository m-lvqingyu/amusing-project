package com.amusing.start.order.controller;

import com.amusing.start.constant.CommonConstant;
import com.amusing.start.order.exception.OrderException;
import com.amusing.start.result.ApiCode;
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
     * @throws OrderException
     */
    public String getUserId() throws OrderException {
        if (request == null) {
            throw new OrderException(ApiCode.UNAUTHORIZED);
        }
        String userUid = request.getHeader(CommonConstant.USER_UID_HEADER_KEY);
        if (StringUtils.isEmpty(userUid)) {
            throw new OrderException(ApiCode.UNAUTHORIZED);
        }
        return userUid;
    }

}
