package com.amusing.start.user.config;

import com.amusing.start.user.entity.pojo.MenuInfo;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Lv.QingYu
 * @since 2024/8/7
 */
@Configuration
public class CacheConfig {

    public static final String MENU_CACHE_KEY = "menu:local:cache";

    @Bean("menuCache")
    public Cache<String, List<MenuInfo>> menuCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .initialCapacity(300)
                .maximumSize(3000)
                .recordStats()
                .build();
    }

}
