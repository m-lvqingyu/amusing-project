package com.amusing.start.admin.controller;

import com.amusing.start.admin.manager.cache.LocalCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;

/**
 * Create By 2021/8/7
 *
 * @author lvqingyu
 */
@RestController
@RequestMapping("cache")
public class CacheController {

    @Autowired
    private LocalCacheManager localCacheManager;

    @PostConstruct
    public void init() {
        String key = "test";
        String initValue = "my love";
        localCacheManager.setRuleData(key, initValue);
    }

    @GetMapping
    public String getLocalCache() throws ExecutionException {
        String key = "test";
        return localCacheManager.getRuleData(key);
    }
}
