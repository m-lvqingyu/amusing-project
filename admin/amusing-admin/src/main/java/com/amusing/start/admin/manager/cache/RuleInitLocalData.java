package com.amusing.start.admin.manager.cache;

import com.amusing.start.cache.local.InitLocalData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Create By 2021/8/7
 *
 * @author lvqingyu
 */
@Slf4j
@Component
public class RuleInitLocalData implements InitLocalData<String, String> {

    @Override
    public String initData(String key) {
        log.info("initData-KEY: ", key);
        return "SUCCESS-INIT-DATA";
    }

    @Override
    public String resetInitData(String key) {
        log.info("resetInitData-KEY: ", key);
        return "SUCCESS-RESET-INIT-DATA";
    }
    
}
