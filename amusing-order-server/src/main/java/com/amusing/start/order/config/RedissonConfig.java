package com.amusing.start.order.config;

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
 * @since 2022/10/5
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

    @Bean
    public RedissonClient redisson() throws NacosException, IOException {
        ConfigService configService = nacosConfigManager.getConfigService();
        String config = configService.getConfig(dataId, group, 3000);
        return Redisson.create(Config.fromYAML(config));
    }

}
