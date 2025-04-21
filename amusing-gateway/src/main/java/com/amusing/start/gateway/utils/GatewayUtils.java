package com.amusing.start.gateway.utils;

import cn.hutool.json.JSONUtil;
import com.amusing.start.code.BaseCode;
import com.amusing.start.gateway.constant.GatewayConstant;
import com.amusing.start.result.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author Lv.QingYu
 * @description: 网关组件工具类
 * @since 2023/02/09
 */
public class GatewayUtils {

    /**
     * X-Forwarded-For: 代理ip地址
     */
    private static final String X_FORWARDED_FOR = "x-forwarded-for";

    /**
     * 一般是经过apache http服务器的请求才会有，用apache http做代理时一般会加上Proxy-Client-IP请求头，而WL- Proxy-Client-IP是他的web logic插件加上的请求头
     */
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";

    /**
     * WebLogic Server使用的代理服务器将客户端的IP地址放在WL-Proxy-Client-IP请求头中
     */
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";

    /**
     * HTTP_CLIENT_IP 是代理服务器发送的HTTP头，HTTP_CLIENT_IP确实存在于http请求的header里。
     */
    private static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";

    /**
     * 向代理服务器:使用Nginx等反向代理服务器时,http_x_forwarded_for可以识别客户端真实IP地址
     */
    private static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";

    private static final String UNKNOWN = "unknown";

    /**
     * @param exchange  HTTP请求和响应上下文
     * @param errorCode 错误码
     * @return 响应结果
     * @description: 验签或者认证失败时，响应结果
     */
    public static Mono<Void> failMono(ServerWebExchange exchange, BaseCode<?> errorCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, GatewayConstant.ASTERISK);
        response.getHeaders().set(HttpHeaders.CACHE_CONTROL, GatewayConstant.NO_CACHE);
        String body = JSONUtil.toJsonStr(ApiResult.result(errorCode));
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * @param request HTTP请求
     * @return IP地址
     * @description: 根据HTTP请求头信息。获取IP地址
     */
    public static String getIp(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst(X_FORWARDED_FOR);
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = headers.getFirst(PROXY_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = headers.getFirst(WL_PROXY_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = headers.getFirst(HTTP_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = headers.getFirst(HTTP_X_FORWARDED_FOR);
        }
        return ip;
    }

}
