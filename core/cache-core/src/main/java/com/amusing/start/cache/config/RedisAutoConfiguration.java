package com.amusing.start.cache.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author ：RedisAutoConfiguration
 * @date ：2022/4/9 18:47
 */
@Configuration
@EnableConfigurationProperties(RedisProps.class)
public class RedisAutoConfiguration {

    @Autowired
    public RedisAutoConfiguration (RedisProps redisProps){
        this.redisProps = redisProps;
    }

    private final RedisProps redisProps;

    @Bean
    public Config config() throws IOException {
        File file = ResourceUtils.getFile(redisProps.getYaml());
        return Config.fromYAML(file);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedissonClient redissonClient(Config config) {
        return Redisson.create(config);
    }


}
