package com.amusing.start.gateway.filter;

import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.constant.AuthConstant;
import com.amusing.start.constant.CommCacheKey;
import com.amusing.start.gateway.config.IgnoreAuthPathConfig;
import com.amusing.start.gateway.utils.GatewayUtils;
import com.amusing.start.utils.TokenUtils;
import com.auth0.jwt.interfaces.Claim;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
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

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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

    private final RedissonClient redissonClient;

    @Autowired
    public AmusingAuthFilter(AntPathMatcher antPathMatcher, IgnoreAuthPathConfig ignoreAuthPathConfig, RedissonClient redissonClient) {
        this.antPathMatcher = antPathMatcher;
        this.ignoreAuthPathConfig = ignoreAuthPathConfig;
        this.redissonClient = redissonClient;
    }

    /**
     * 角色-Path映射关系本地缓存（60秒）
     */
    private LoadingCache<Integer, List<String>> rolePathMappingMap;

    /**
     * 角色-Path映射关系本地缓存初始化
     */
    @PostConstruct
    public void init() {
        rolePathMappingMap = CacheBuilder.newBuilder()
                // 初始容量
                .initialCapacity(50)
                // 最大容量
                .maximumSize(2000)
                // 在缓存写入一定时间后，再次访问会先使用CacheLoader去刷新缓存；如果刷新失败，或者有其他任务正在刷新缓存，则会返回现有的缓存值
                .refreshAfterWrite(60, TimeUnit.SECONDS)
                .recordStats()
                .build(new CacheLoader<Integer, List<String>>() {
                    @Override
                    public List<String> load(Integer roleId) throws Exception {
                        String cacheKey = CommCacheKey.roleMenuMapping();
                        RMap<Integer, List<String>> rMap = redissonClient.getMap(cacheKey);
                        List<String> pathList = rMap.get(roleId);
                        if (CollectionUtil.isEmpty(pathList)) {
                            return new ArrayList<>();
                        }
                        return pathList;
                    }
                });
        String cacheKey = CommCacheKey.roleMenuMapping();
        RMap<Integer, List<String>> rMap = redissonClient.getMap(cacheKey);
        if (CollectionUtil.isEmpty(rMap)) {
            return;
        }
        for (Map.Entry<Integer, List<String>> next : rMap.entrySet()) {
            Integer key = next.getKey();
            List<String> value = next.getValue();
            value = CollectionUtil.isEmpty(value) ? new ArrayList<>() : value;
            rolePathMappingMap.put(key, value);
        }
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
                if (antPathMatcher.match(path, uri)) {
                    return chain.filter(exchange);
                }
            }
        }
        // 用户身份信息认证，根据Token信息，获取用户ID
        String authToken = request.getHeaders().getFirst(AuthConstant.AUTHORIZATION);
        if (StringUtils.isEmpty(authToken)) {
            return GatewayUtils.failMono(exchange, ErrorCode.UNAUTHORIZED);
        }
        Map<String, Claim> claimMap = TokenUtils.getClaims(authToken);
        if (CollectionUtil.isEmpty(claimMap)) {
            return GatewayUtils.failMono(exchange, ErrorCode.UNAUTHORIZED);
        }
        String userId = TokenUtils.getUserId(claimMap);
        if (StringUtils.isEmpty(userId)) {
            return GatewayUtils.failMono(exchange, ErrorCode.UNAUTHORIZED);
        }
        Boolean admin = TokenUtils.getAdmin(claimMap);
        if (admin != null && admin) {
            return buildSuccessMono(exchange, chain, userId);
        }

        Integer[] roleIds = TokenUtils.getRoleIds(claimMap);
        if (roleIds == null || roleIds.length <= 0) {
            return GatewayUtils.failMono(exchange, ErrorCode.UNAUTHORIZED);

        }
        boolean flag = false;
        for (Integer roleId : roleIds) {
            List<String> pathList;
            try {
                pathList = rolePathMappingMap.get(roleId);
            } catch (ExecutionException e) {
                log.error("[AmusingAuthFilter]-getRolePathMapping err! roleId:{}, mag:{}", roleId, Throwables.getStackTraceAsString(e));
                return GatewayUtils.failMono(exchange, ErrorCode.UNAUTHORIZED);
            }
            if (CollectionUtils.isEmpty(pathList)) {
                continue;
            }

            for (String path : pathList) {
                if (antPathMatcher.match(path, uri)) {
                    flag = true;
                    break;
                }
            }
        }

        if (flag) {
            return buildSuccessMono(exchange, chain, userId);
        }
        return GatewayUtils.failMono(exchange, ErrorCode.UNAUTHORIZED);
    }

    private Mono<Void> buildSuccessMono(ServerWebExchange exchange, GatewayFilterChain chain, String userId) {
        // 将用户ID封装在请求头中，方便后续服务获取
        ServerHttpRequest newRequest = exchange.getRequest().mutate().header(AuthConstant.USER_UID, userId).build();
        return chain.filter(exchange.mutate().request(newRequest).build());
    }

}
