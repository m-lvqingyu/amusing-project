package com.amusing.start.order.handler;

import com.amusing.start.annotation.RequestLimit;
import com.amusing.start.code.CommCode;
import com.amusing.start.constant.AuthConstant;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.order.constant.CacheKey;
import org.apache.commons.lang3.StringUtils;
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
    public boolean preHandle(@Nonnull HttpServletRequest request,
                             @Nonnull HttpServletResponse response,
                             @Nonnull Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }
        HandlerMethod method = (HandlerMethod) handler;
        RequestLimit requestLimit = method.getMethodAnnotation(RequestLimit.class);
        if (requestLimit == null) {
            return super.preHandle(request, response, handler);
        }
        String userId = request.getHeader(AuthConstant.USER_UID);
        if (StringUtils.isBlank(userId)) {
            throw new CustomException(CommCode.UNAUTHORIZED);
        }
        String cacheKey = CacheKey.reqLimitKey(userId, request.getRequestURI());
        RAtomicLong atomicLong = redissonClient.getAtomicLong(cacheKey);
        if (atomicLong == null) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        long count = atomicLong.get();
        if (count > requestLimit.count()) {
            throw new CustomException(CommCode.REQUEST_LIMIT);
        }
        // 第一次访问
        if (count == CommConstant.ZERO) {
            atomicLong.set(CommConstant.ONE);
            atomicLong.expire(requestLimit.time(), TimeUnit.SECONDS);
            return super.preHandle(request, response, handler);
        }
        atomicLong.incrementAndGet();
        return super.preHandle(request, response, handler);
    }

}
