package com.amusing.start.gateway.filter;

import com.amusing.start.constant.CommConstant;
import com.amusing.start.response.UserLoginResponse;
import com.amusing.start.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * @author Lv.QingYu
 * @since 2021/09/04
 */
@Slf4j
@Component
public class AmusingTokenParseFilter implements GlobalFilter {

    /**
     * @param exchange HTTP请求和响应上下文
     * @param chain    Gateway网关过滤器链
     * @return 响应结果
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authToken = request.getHeaders().getFirst(CommConstant.AUTHORIZATION);
        if (StringUtils.isBlank(authToken)) {
            return chain.filter(exchange);
        }
        UserLoginResponse loginResponse = TokenUtils.parse(authToken);
        String userId = loginResponse.getUserId();
        String version = String.valueOf(loginResponse.getVersion());
        ServerHttpRequest newRequest = exchange.getRequest().mutate()
                .header(CommConstant.USER_UID, userId)
                .header(CommConstant.LOGIN_VERSION, version).build();
        return chain.filter(exchange.mutate().request(newRequest).build());
    }

}
