package com.amusing.start.user.config;

import com.amusing.start.user.handler.RequestLimitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by  2023/2/15.
 *
 * @author lvqingyu
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestLimitHandler requestLimitHandler;

    @Autowired
    public WebConfig(RequestLimitHandler requestLimitHandler) {
        this.requestLimitHandler = requestLimitHandler;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLimitHandler);
    }

}
