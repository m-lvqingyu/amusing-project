package com.amusing.start.gateway.constant;

/**
 * @author Lv.QingYu
 * @since 2023/8/28
 */
public class GatewayConstant {

    /**
     * POST请求-请求参数attributes的key
     */
    public static final String REQUEST_BODY_PARAM = "REQUEST_BODY_PARAM";

    /**
     * HTTP响应头，指定服务器端允许进行跨域资源访问的来源域。可以用通配符*表示允许任何域的JavaScript访问资源
     */
    public static final String ASTERISK = "*";

    /**
     * 可以在客户端存储资源，每次都必须去服务端做新鲜度校验
     */
    public static final String NO_CACHE = "no-cache";

    /**
     * 用来指定本次预检请求的有效期,单位为秒,,在此期间不用发出另一条预检请求
     */
    public static final String ACCESS_CONTROL_MAX_AGE_TIME = "18000L";


}
