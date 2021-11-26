package com.amusing.start.map.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Create By 2021/11/26
 *
 * @author lvqingyu
 */
@Component
public class RestTemplateConfig {

    /**
     * 自定义RestTemplate配置
     *
     * @return restTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(1500);
        factory.setReadTimeout(2000);
        return new RestTemplate(factory);
    }

}
