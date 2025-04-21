package com.amusing.start.user.utils;

import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Lv.QingYu
 * @since 2024/12/4
 */

@Component
public class IdCustomUtils {

    @Value("${user.worker}")
    private Long workerId;

    @Value("${user.dataCenter}")
    private Long dataCenterId;

    public String generateUserId() {
        return IdUtil.createSnowflake(workerId, dataCenterId).nextIdStr();
    }
    
}
