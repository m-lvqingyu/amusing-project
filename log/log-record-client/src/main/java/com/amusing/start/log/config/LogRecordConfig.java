package com.amusing.start.log.config;

import com.amusing.start.log.aspect.LogRecordAspect;
import com.amusing.start.log.task.LogRecordTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Create By 2021/7/24
 *
 * @author lvqingyu
 */
@EnableAsync
@Configuration
public class LogRecordConfig {

    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final int MAX_POOL_SIZE = CORE_POOL_SIZE * 2;
    private static final int KEEP_ALIVE_TIME = 10;
    private static final int QUEUE_CAPACITY = 200;
    private static final String THREAD_NAME_PREFIX = "Async-Service-";

    @Bean("logRecordTaskExecutor")
    public ThreadPoolTaskExecutor logRecordTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    public LogRecordTask logRecordTask(){
        return new LogRecordTask();
    }

    @Bean
    public LogRecordAspect logRecordAspect() {
        return new LogRecordAspect(logRecordTask());
    }

}
