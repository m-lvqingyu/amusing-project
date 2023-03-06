package com.amusing.start.gateway.utils;

import cn.hutool.json.JSONUtil;
import com.amusing.start.code.ErrorCode;
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
 * Created by 2023/2/9.
 *
 * @author lvqingyu
 */
public class GatewayUtils {

    public static final String SIGNATURE = "signature";

    public static final String TIMESTAMP = "timestamp";

    public static final int FIVE_MILLISECONDS = 5000;

    public static final String REQUEST_BODY_PARAM = "REQUEST_BODY_PARAM";

    private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    private static final String ASTERISK = "*";

    private static final String CACHE_CONTROL = "Cache-Control";

    private static final String NO_CACHE = "no-cache";

    private static final String X_FORWARDED_FOR = "x-forwarded-for";

    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";

    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";

    private static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";

    private static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";


    public static Mono<Void> failMono(ServerWebExchange exchange, ErrorCode errorCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set(ACCESS_CONTROL_ALLOW_ORIGIN, ASTERISK);
        response.getHeaders().set(CACHE_CONTROL, NO_CACHE);
        String body = JSONUtil.toJsonStr(ApiResult.result(errorCode));
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    public static String getIp(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst(X_FORWARDED_FOR);
        String unknown = "unknown";
        if (StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = headers.getFirst(PROXY_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = headers.getFirst(WL_PROXY_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = headers.getFirst(HTTP_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = headers.getFirst(HTTP_X_FORWARDED_FOR);
        }
        return ip;
    }

}
