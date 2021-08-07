package com.amusing.start.admin.manager.cache;

import com.amusing.start.cache.local.InitLocalData;
import com.amusing.start.cache.local.LocalCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * Create By 2021/8/7
 *
 * @author lvqingyu
 */
@Slf4j
@Component
public class LocalCacheManager extends LocalCache<String, String> {

    @Autowired
    public LocalCacheManager(RuleInitLocalData ruleInitLocalData) {
        super(ruleInitLocalData);
    }


    public void setRuleData(String key, String value) {
        super.setData(key, value);
    }

    public String getRuleData(String key) throws ExecutionException {
        return super.getData(key);
    }

}
