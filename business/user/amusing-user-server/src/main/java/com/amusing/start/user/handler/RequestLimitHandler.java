package com.amusing.start.user.handler;

import com.amusing.start.code.CommCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.log.RequestUtils;
import com.amusing.start.annotation.RequestLimit;
import com.amusing.start.user.constant.CacheKey;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author Lv.QingYu
 * @description: 接口访问频次限制处理器
 * @since 2023/03/06
 */
@Component
public class RequestLimitHandler extends HandlerInterceptorAdapter {

    private static final int DEF_REQUEST_COUNT = 1;

    private final RedissonClient redissonClient;

    @Autowired
    RequestLimitHandler(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return true:成功 false:失败
     * @throws Exception 异常信息
     * @description: 通过接口上的注解：RequestLimit，判断接口请求次数是否超过限制
     */
    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }
        HandlerMethod hm = (HandlerMethod) handler;
        RequestLimit requestLimit = hm.getMethodAnnotation(RequestLimit.class);
        if (requestLimit == null) {
            return super.preHandle(request, response, handler);
        }
        String cacheKey = CacheKey.reqLimitCacheKey(RequestUtils.getIp(request), request.getRequestURI());
        RAtomicLong atomicLong = redissonClient.getAtomicLong(cacheKey);
        long count = atomicLong.get();
        if(count > requestLimit.count()) {
            throw new CustomException(CommCode.REQUEST_LIMIT);
        }
        // 第一次访问
        if (count == CommConstant.ZERO) {
            atomicLong.set(DEF_REQUEST_COUNT);
            atomicLong.expire(requestLimit.time(), TimeUnit.SECONDS);
            return super.preHandle(request, response, handler);
        }
        atomicLong.incrementAndGet();
        return super.preHandle(request, response, handler);
    }

}
