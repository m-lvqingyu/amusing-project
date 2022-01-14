package com.amusing.start.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Create By 2021/9/5
 *
 * @author lvqingyu
 */
@Configuration
@ConfigurationProperties(prefix = "ignore.auth")
@EnableConfigurationProperties({IgnoreAuthPathConfig.class})
public class IgnoreAuthPathConfig {

    private List<String> paths;

    public List<String> getPaths() {
        return paths;
    }
    
    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}
