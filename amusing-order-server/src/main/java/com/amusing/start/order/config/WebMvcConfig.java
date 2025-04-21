package com.amusing.start.order.config;

import com.amusing.start.log.TraceIdFilter;
import com.amusing.start.log.TraceLogAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Lv.QingYu
 * @since 2022/11/26
 */
@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public TraceIdFilter traceIdFilter() {
        return new TraceIdFilter();
    }

    @Bean
    public TraceLogAspect traceLogAspect() {
        return new TraceLogAspect();
    }
}
