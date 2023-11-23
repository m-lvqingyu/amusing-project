package com.amusing.start.gateway.filter;

import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.code.CommCode;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.constant.AuthConstant;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.gateway.config.props.IgnoreAuthPathProps;
import com.amusing.start.gateway.config.props.RolePathMappingProps;
import com.amusing.start.gateway.utils.GatewayUtils;
import com.amusing.start.utils.TokenUtils;
import com.auth0.jwt.interfaces.Claim;
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
import java.util.Map;

/**
 * @author Lv.QingYu
 * @description: 认证以及授权过滤器
 * @since 2021/09/04
 */
@Slf4j
@Order(Integer.MIN_VALUE + 2)
@Component
public class AmusingAuthFilter implements GlobalFilter {

    private final AntPathMatcher antPathMatcher;

    private final IgnoreAuthPathProps ignoreAuthPathProps;

    private final RolePathMappingProps rolePathMappingProps;

    @Autowired
    public AmusingAuthFilter(AntPathMatcher antPathMatcher,
                             IgnoreAuthPathProps ignoreAuthPathProps,
                             RolePathMappingProps rolePathMappingProps) {
        this.antPathMatcher = antPathMatcher;
        this.ignoreAuthPathProps = ignoreAuthPathProps;
        this.rolePathMappingProps = rolePathMappingProps;
    }

    /**
     * @param exchange HTTP请求和响应上下文
     * @param chain    Gateway网关过滤器链
     * @return 响应结果
     * @description: 用户身份信息认证以及授权
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 请求地址白名单校验
        String uri = request.getURI().getPath();
        List<String> paths = ignoreAuthPathProps.getPaths();
        if (CollectionUtil.isNotEmpty(paths)) {
            for (String path : paths) {
                if (antPathMatcher.match(path, uri)) {
                    return chain.filter(exchange);
                }
            }
        }
        // 用户身份信息认证，根据Token信息，获取用户ID
        String authToken = request.getHeaders().getFirst(AuthConstant.AUTHORIZATION);
        if (StringUtils.isEmpty(authToken)) {
            log.warn("check token fail! token is null. path:{}", uri);
            return GatewayUtils.failMono(exchange, CommCode.UNAUTHORIZED);
        }
        Map<String, Claim> claimMap = TokenUtils.getClaims(authToken);
        if (CollectionUtil.isEmpty(claimMap)) {
            log.warn("check token fail! unable to obtain token properties. token:{}", authToken);
            return GatewayUtils.failMono(exchange, CommCode.UNAUTHORIZED);
        }
        String userId = TokenUtils.getUserId(claimMap);
        if (StringUtils.isEmpty(userId)) {
            log.warn("check token fail! unable to obtain userId by token. token:{}", authToken);
            return GatewayUtils.failMono(exchange, CommCode.UNAUTHORIZED);
        }
        // 判断是否是管理员账户
        Boolean admin = TokenUtils.getAdmin(claimMap);
        if (admin != null && admin) {
            return buildSuccessMono(exchange, chain, userId);
        }
        // 请求路径授权验证
        boolean flag = pathAuth(TokenUtils.getRoleIdList(claimMap), uri);
        if (!flag) {
            log.warn("path auth fail! userId:{}, path:{}", userId, uri);
            return GatewayUtils.failMono(exchange, CommCode.UNAUTHORIZED);
        }
        return buildSuccessMono(exchange, chain, userId);
    }

    private Mono<Void> buildSuccessMono(ServerWebExchange exchange, GatewayFilterChain chain, String userId) {
        // 将用户ID封装在请求头中，方便后续服务获取
        ServerHttpRequest newRequest = exchange.getRequest().mutate().header(AuthConstant.USER_UID, userId).build();
        return chain.filter(exchange.mutate().request(newRequest).build());
    }

    /**
     * @param roleIdList 角色列表
     * @param uri        请求路径
     * @return true:通过 false:失败
     * @description: 请求路径授权
     */
    public Boolean pathAuth(Integer[] roleIdList, String uri) {
        if (roleIdList == null || roleIdList.length == CommConstant.ZERO) {
            return Boolean.FALSE;
        }
        List<RolePathMappingProps.RolePathMapping> mapping = rolePathMappingProps.getMapping();
        if (CollectionUtil.isEmpty(mapping)) {
            return Boolean.FALSE;
        }
        for (Integer roleId : roleIdList) {
            List<String> pathList = null;
            for (RolePathMappingProps.RolePathMapping rolePathMapping : mapping) {
                if (rolePathMapping.getCode().equals(roleId)) {
                    pathList = rolePathMapping.getUris();
                    break;
                }
            }
            if (CollectionUtil.isEmpty(pathList)) {
                continue;
            }
            for (String path : pathList) {
                if (antPathMatcher.match(path, uri)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

}
