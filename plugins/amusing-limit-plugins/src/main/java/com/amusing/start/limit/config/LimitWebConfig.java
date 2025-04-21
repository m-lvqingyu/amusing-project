package com.amusing.start.limit.config;

import cn.hutool.core.util.CharsetUtil;
import com.amusing.start.limit.handler.RequestLimitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@ConditionalOnProperty(name = "spring.limit.enabled", havingValue = "true")
public class LimitWebConfig implements WebMvcConfigurer {

    private final RequestLimitHandler requestLimitHandler;

    @Autowired
    public LimitWebConfig(RequestLimitHandler requestLimitHandler) {
        this.requestLimitHandler = requestLimitHandler;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(@Nonnull HttpServletRequest request,
                                     @Nonnull HttpServletResponse response,
                                     @Nonnull Object handler) throws Exception {
                request.setCharacterEncoding(CharsetUtil.UTF_8);
                return Boolean.TRUE;
            }
        });
        registry.addInterceptor(requestLimitHandler);
    }

}
