package com.amusing.start.limit.handler;

import com.amusing.start.code.CommunalCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.limit.annotation.RequestLimit;
import com.amusing.start.utils.RequestUtils;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RequestLimitHandler implements HandlerInterceptor {

    private static final int DEF_REQUEST_COUNT = 1;

    private static final String REQ_LIMIT_CACHE_PREFIX = "request_limit:";

    private final RedissonClient redissonClient;

    public RequestLimitHandler(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 通过接口上的注解：RequestLimit，判断接口请求次数是否超过限制
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return true:成功 false:失败
     */
    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request,
                             @Nonnull HttpServletResponse response,
                             @Nonnull Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return Boolean.TRUE;
        }
        HandlerMethod hm = (HandlerMethod) handler;
        RequestLimit requestLimit = hm.getMethodAnnotation(RequestLimit.class);
        if (requestLimit == null) {
            return Boolean.TRUE;
        }
        String cacheKey = limitCacheKey(RequestUtils.getIp(request), request.getRequestURI());
        RAtomicLong atomicLong = null;
        try {
            atomicLong = redissonClient.getAtomicLong(cacheKey);
        } catch (Exception e) {
            log.error("[Limit]-err! msg:{}", Throwables.getStackTraceAsString(e));
        }
        if (atomicLong == null) {
            return Boolean.TRUE;
        }
        long count = atomicLong.get();
        if (count > requestLimit.count()) {
            throw new CustomException(CommunalCode.REQUEST_LIMIT);
        }
        if (count == CommConstant.ZERO) {
            atomicLong.set(DEF_REQUEST_COUNT);
            atomicLong.expire(requestLimit.time(), TimeUnit.SECONDS);
            return Boolean.TRUE;
        }
        atomicLong.incrementAndGet();
        return Boolean.TRUE;
    }

    public String limitCacheKey(String ip, String uri) {
        return REQ_LIMIT_CACHE_PREFIX + ip + ":" + uri;
    }

}
