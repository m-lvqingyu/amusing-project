package com.amusing.start.cache.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：RedisProperties
 * @date ：2022/4/9 18:41
 */
@Configuration
@ConfigurationProperties("spring.redisson")
public class RedisProps {

    private String yaml;

    public String getYaml() {
        return yaml;
    }

    public void setYaml(String yaml) {
        this.yaml = yaml;
    }
}
