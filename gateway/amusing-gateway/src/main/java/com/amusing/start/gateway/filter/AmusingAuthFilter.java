package com.amusing.start.gateway.filter;

import cn.hutool.json.JSONUtil;
import com.amusing.start.constant.CommonConstant;
import com.amusing.start.gateway.config.TokenWhiteListConfig;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import com.amusing.start.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Create By 2021/9/4
 *
 * @author lvqingyu
 */
@Component
public class AmusingAuthFilter implements GlobalFilter {

    private final AntPathMatcher antPathMatcher;

    private final TokenWhiteListConfig tokenWhiteListConfig;

    @Autowired
    public AmusingAuthFilter(AntPathMatcher antPathMatcher,
                             TokenWhiteListConfig tokenWhiteListConfig) {
        this.antPathMatcher = antPathMatcher;
        this.tokenWhiteListConfig = tokenWhiteListConfig;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String path = request.getURI().getPath();
        // 地址白名单校验
        boolean isWhitePath = checkoutWhiteList(path);
        if (isWhitePath) {
            return chain.filter(exchange);
        }
        // Token校验
        String authToken = headers.getFirst(CommonConstant.AUTH_TOKEN);
        if (StringUtils.isEmpty(authToken)) {
            return checkoutFail(exchange);
        }
        String userId = TokenUtils.getUserId(authToken);
        if (StringUtils.isEmpty(userId)) {
            return checkoutFail(exchange);
        }
        ServerHttpRequest newRequest = exchange
                .getRequest()
                .mutate()
                .header(CommonConstant.USER_UID_HEADER_KEY, userId)
                .build();
        return chain.filter(exchange.mutate().request(newRequest).build());
    }

    private boolean checkoutWhiteList(String path) {
        List<String> ignoreWhiteList = tokenWhiteListConfig.getList();
        if (ignoreWhiteList == null || ignoreWhiteList.isEmpty()) {
            return false;
        }
        for (String uri : ignoreWhiteList) {
            boolean match = antPathMatcher.match(uri, path);
            if (match) {
                return true;
            }
        }
        return false;
    }

    private Mono<Void> checkoutFail(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set("Access-Control-Allow-Origin", "*");
        response.getHeaders().set("Cache-Control", "no-cache");
        String body = JSONUtil.toJsonStr(ApiResult.result(CommCode.UNAUTHORIZED));
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
        return response.writeWith(Mono.just(buffer));
    }
}
