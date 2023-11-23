package com.amusing.start.user.config;

import com.amusing.start.user.handler.RequestLimitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Lv.QingYu
 * @description: WebMvc配置
 * @since 2022/11/26
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 接口频次拦截器
     */
    private final RequestLimitHandler requestLimitHandler;

    @Autowired
    public WebConfig(RequestLimitHandler requestLimitHandler) {
        this.requestLimitHandler = requestLimitHandler;
    }

    /**
     * @param registry 拦截器注册对象
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册接口频次拦截器
        registry.addInterceptor(requestLimitHandler);
    }

    /**
     * @return 密码编解码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
