package com.amusing.start.platform.constant;

/**
 * @author Lv.QingYu
 * @description: 钉钉错误码
 * @since 2023/10/8
 */
public class DingTalkErrorCode {

    public static final long SUCCESS = 0L;

    /**
     * 系统繁忙
     */
    public static final long SYSTEM_BUSY = -1L;

    /**
     * 未找到该用户
     */
    public static final long USER_NOT_FOUND = 60121L;

    /**
     * 企业中无效的手机号
     */
    public static final long INVALID_PHONE = 40104L;

    /**
     * 无效的参数
     */
    public static final long INVALID_PARAM = 400002L;

}
