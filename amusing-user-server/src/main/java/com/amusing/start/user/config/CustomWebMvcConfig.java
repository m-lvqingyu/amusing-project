package com.amusing.start.user.config;

import com.amusing.start.config.AmusingAuthInterceptor;
import com.amusing.start.log.TraceIdFilter;
import com.amusing.start.log.TraceLogAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Lv.QingYu
 * @description: WebMvc配置
 * @since 2022/11/26
 */
@RequiredArgsConstructor
@Configuration
public class CustomWebMvcConfig implements WebMvcConfigurer {

    private final AmusingAuthInterceptor amusingAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(amusingAuthInterceptor).addPathPatterns("/**");
    }

    @Bean
    public TraceIdFilter traceIdFilter() {
        return new TraceIdFilter();
    }

    @Bean
    public TraceLogAspect traceLogAspect() {
        return new TraceLogAspect();
    }
}
