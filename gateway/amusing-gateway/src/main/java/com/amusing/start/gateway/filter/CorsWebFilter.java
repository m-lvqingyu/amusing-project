package com.amusing.start.gateway.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author ：lv.qingyu
 * @date ：2022/5/14 19:57
 * 跨域设置
 */
@Component
public class CorsWebFilter implements WebFilter {

    private static final String ALL = "*";

    private static final String MAX_AGE = "18000L";

    private static final String FLAG = "true";

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        if (!CorsUtils.isCorsRequest(request)) {
            return webFilterChain.filter(serverWebExchange);
        }
        HttpHeaders headers = request.getHeaders();
        ServerHttpResponse response = serverWebExchange.getResponse();
        HttpMethod requestMethod = headers.getAccessControlRequestMethod();
        HttpHeaders responseHeaders = response.getHeaders();
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, headers.getOrigin());
        responseHeaders.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, headers.getAccessControlRequestHeaders());
        if (requestMethod != null) {
            responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
        }
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, FLAG);
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, ALL);
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
        if (request.getMethod() == HttpMethod.OPTIONS) {
            response.setStatusCode(HttpStatus.OK);
            return Mono.empty();
        }
        return webFilterChain.filter(serverWebExchange);
    }

}
