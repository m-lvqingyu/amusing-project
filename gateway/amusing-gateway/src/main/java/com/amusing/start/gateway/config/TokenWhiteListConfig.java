package com.amusing.start.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
@ConfigurationProperties(prefix = "ignore.white")
@EnableConfigurationProperties({TokenWhiteListConfig.class})
public class TokenWhiteListConfig {

    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
