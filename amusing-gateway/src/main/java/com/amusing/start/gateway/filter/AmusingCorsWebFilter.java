package com.amusing.start.gateway.filter;

import com.amusing.start.gateway.constant.GatewayConstant;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author Lv.QingYu
 * @since 2021/09/04
 */
@Component
public class AmusingCorsWebFilter implements WebFilter {

    /**
     * 跨域请求配置
     *
     * @param serverWebExchange HTTP请求和响应上下文
     * @param webFilterChain    过滤器链
     * @return 响应结果
     */
    @NonNull
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, @NonNull WebFilterChain webFilterChain) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        if (!CorsUtils.isCorsRequest(request)) {
            return webFilterChain.filter(serverWebExchange);
        }
        HttpHeaders headers = request.getHeaders();
        ServerHttpResponse response = serverWebExchange.getResponse();
        HttpHeaders responseHeaders = response.getHeaders();
        // 允许一个资源可以被哪些源站访问
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, headers.getOrigin());
        // 规定服务器允许请求携带哪些头部信息的
        responseHeaders.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, headers.getAccessControlRequestHeaders());
        // Access-Control-Allow-Methods是一个HTTP响应头部，用于指定在预检请求中允许的HTTP方法
        HttpMethod requestMethod = headers.getAccessControlRequestMethod();
        if (requestMethod != null) {
            responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
        }
        // 是否可以将对请求的响应暴露给页面。返回true则可以，其他值均不可以
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, Boolean.TRUE.toString());
        // 响应报头 指示哪些报头可以 公开 为通过列出他们的名字的响应的一部分
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, GatewayConstant.ASTERISK);
        // 用来指定本次预检请求的有效期,单位为秒,,在此期间不用发出另一条预检请求
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, GatewayConstant.ACCESS_CONTROL_MAX_AGE_TIME);
        if (request.getMethod() == HttpMethod.OPTIONS) {
            response.setStatusCode(HttpStatus.OK);
            return Mono.empty();
        }
        return webFilterChain.filter(serverWebExchange);
    }

}
