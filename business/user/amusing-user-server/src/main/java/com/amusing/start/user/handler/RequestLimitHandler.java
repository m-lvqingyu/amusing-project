package com.amusing.start.user.handler;

import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.log.RequestUtils;
import com.amusing.start.user.annotation.RequestLimit;
import com.amusing.start.user.constant.CacheKey;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * Created by 2023/2/15.
 *
 * @author lvqingyu
 */
@Component
public class RequestLimitHandler extends HandlerInterceptorAdapter {

    private static final int DEF_REQUEST_COUNT = 1;

    private final RedissonClient redissonClient;

    @Autowired
    RequestLimitHandler(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }
        HandlerMethod hm = (HandlerMethod) handler;
        RequestLimit requestLimit = hm.getMethodAnnotation(RequestLimit.class);
        if (requestLimit == null) {
            return super.preHandle(request, response, handler);
        }
        String cacheKey = CacheKey.requestLimitKey(RequestUtils.getIp(request), request.getRequestURI());
        RBucket<Integer> bucket = redissonClient.getBucket(cacheKey);
        Integer count = bucket.get();
        if (count == null) {
            bucket.set(DEF_REQUEST_COUNT, requestLimit.time(), TimeUnit.SECONDS);
            return super.preHandle(request, response, handler);
        }
        if (count > requestLimit.count()) {
            throw new CustomException(ErrorCode.REQUEST_LIMIT);
        }
        bucket.set(DEF_REQUEST_COUNT + count);
        return super.preHandle(request, response, handler);
    }

}
