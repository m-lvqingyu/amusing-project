package com.amusing.start.log.config;

import com.amusing.start.log.aspect.OperationLogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2024/8/6
 */
@Configuration
public class LogConfig {

    @Bean("operationLogAspect")
    public OperationLogAspect operationLogAspect() {
        return new OperationLogAspect();
    }

}
