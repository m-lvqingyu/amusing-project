package com.amusing.start.gateway.filter;

import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.client.api.UserClient;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.constant.AuthConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.gateway.config.IgnoreAuthPathConfig;
import com.amusing.start.gateway.utils.GatewayUtils;
import com.amusing.start.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Create By 2021/9/4
 *
 * @author lvqingyu
 */
@Slf4j
@Order(Integer.MIN_VALUE + 2)
@Component
public class AmusingAuthFilter implements GlobalFilter {

    private final AntPathMatcher antPathMatcher;

    private final IgnoreAuthPathConfig ignoreAuthPathConfig;

    private final UserClient userClient;

    @Autowired
    public AmusingAuthFilter(AntPathMatcher antPathMatcher,
                             IgnoreAuthPathConfig ignoreAuthPathConfig,
                             UserClient userClient) {
        this.antPathMatcher = antPathMatcher;
        this.ignoreAuthPathConfig = ignoreAuthPathConfig;
        this.userClient = userClient;
    }

    /**
     * 用户身份信息以及权限验证
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("[AmusingAuthFilter]-filter start time:{}", System.currentTimeMillis());
        ServerHttpRequest request = exchange.getRequest();
        // 请求地址白名单校验
        String uri = request.getURI().getPath();
        List<String> paths = ignoreAuthPathConfig.getPaths();
        if (CollectionUtil.isNotEmpty(paths)) {
            for (String path : paths) {
                if (antPathMatcher.match(uri, path)) {
                    return chain.filter(exchange);
                }
            }
        }
        // 用户身份信息认证，根据Token信息，获取用户ID
        String authToken = request.getHeaders().getFirst(AuthConstant.AUTHORIZATION);
        if (StringUtils.isEmpty(authToken)) {
            return GatewayUtils.failMono(exchange, ErrorCode.UNAUTHORIZED);
        }
        String userId = TokenUtils.getUserId(authToken);
        if (StringUtils.isEmpty(userId)) {
            return GatewayUtils.failMono(exchange, ErrorCode.UNAUTHORIZED);
        }
        // 用户权限校验
        try {
            userClient.matchPath(userId, uri);
        } catch (CustomException e) {
            return GatewayUtils.failMono(exchange, e.getErrorCode());
        }
        // 将用户ID封装在请求头中，方便后续服务获取
        ServerHttpRequest newRequest = exchange.getRequest().mutate().header(AuthConstant.USER_UID, userId).build();
        return chain.filter(exchange.mutate().request(newRequest).build());
    }

}
