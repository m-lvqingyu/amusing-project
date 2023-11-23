package com.amusing.start.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author Lv.QingYu
 * @description: redis相关配置
 * @since 2022/10/05
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redisson.dataId}")
    private String dataId;

    @Value("${spring.redisson.group}")
    private String group;

    private final NacosConfigManager nacosConfigManager;

    @Autowired
    public RedissonConfig(NacosConfigManager nacosConfigManager) {
        this.nacosConfigManager = nacosConfigManager;
    }

    private static final long TIMEOUT = 3000L;

    @Bean
    public RedissonClient redisson() throws NacosException, IOException {
        ConfigService configService = nacosConfigManager.getConfigService();
        String config = configService.getConfig(dataId, group, TIMEOUT);
        return Redisson.create(Config.fromYAML(config));
    }

}
