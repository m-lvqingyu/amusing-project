package com.amusing.start.gateway.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.amusing.start.code.CommCode;
import com.amusing.start.constant.CommonConstant;
import com.amusing.start.gateway.config.IgnoreAuthPathConfig;
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

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Create By 2021/9/4
 *
 * @author lvqingyu
 */
@Component
public class AmusingAuthFilter implements GlobalFilter {

    private final AntPathMatcher antPathMatcher;

    private final IgnoreAuthPathConfig ignoreAuthPathConfig;

    @Autowired
    public AmusingAuthFilter(AntPathMatcher antPathMatcher, IgnoreAuthPathConfig ignoreAuthPathConfig) {
        this.antPathMatcher = antPathMatcher;
        this.ignoreAuthPathConfig = ignoreAuthPathConfig;
    }

    private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    private static final String ASTERISK = "*";

    private static final String CACHE_CONTROL = "Cache-Control";

    private static final String NO_CACHE = "no-cache";

    /**
     * 用户身份信息认证
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 请求地址白名单校验
        if (isIgnorePath(request.getURI().getPath())) {
            return chain.filter(exchange);
        }
        // 用户身份信息认证
        HttpHeaders headers = request.getHeaders();
        String authToken = headers.getFirst(CommonConstant.AUTHORIZATION);
        if (StringUtils.isEmpty(authToken) || !authToken.startsWith(CommonConstant.BEARER)) {
            return authorizationFail(exchange);
        }
        // 根据Token信息，获取用户ID
        authToken = authToken.substring(CommonConstant.BEARER.length());
        String userId = TokenUtils.getUserId(authToken);
        if (StringUtils.isEmpty(userId)) {
            return authorizationFail(exchange);
        }
        // 将用户ID封装在请求头中，方便后续服务获取
        ServerHttpRequest newRequest = exchange.getRequest().mutate().header(CommonConstant.USER_UID, userId).build();
        return chain.filter(exchange.mutate().request(newRequest).build());
    }

    /**
     * 判断是否需要忽略Token校验的地址
     *
     * @param path 请求地址
     * @return true 无需Token校验  false 需要Token校验
     */
    private boolean isIgnorePath(String path) {
        List<String> paths = ignoreAuthPathConfig.getPaths();
        if (CollectionUtil.isEmpty(paths)) {
            return false;
        }
        for (String uri : paths) {
            if (antPathMatcher.match(uri, path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Token认证失败响应
     *
     * @param exchange 请求/响应封装
     * @return
     */
    private Mono<Void> authorizationFail(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set(ACCESS_CONTROL_ALLOW_ORIGIN, ASTERISK);
        response.getHeaders().set(CACHE_CONTROL, NO_CACHE);
        String body = JSONUtil.toJsonStr(ApiResult.result(CommCode.UNAUTHORIZED));
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
