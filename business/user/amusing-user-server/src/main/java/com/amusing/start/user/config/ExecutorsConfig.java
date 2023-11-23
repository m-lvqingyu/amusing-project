package com.amusing.start.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Lv.QingYu
 * @description: 线程池配置
 * @since 2023/9/1
 */
@Configuration
public class ExecutorsConfig {

    private static final String THREAD_NAME_PREFIX = "CommExecutor-";

    private static final int KEEP_ALIVE_SECONDS = 300;

    private static final int CPU_CORES = Runtime.getRuntime().availableProcessors();

    private static final int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    private static final int QUEUE_CAPACITY = Runtime.getRuntime().availableProcessors() * 10;

    @Bean
    public Executor commExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 线程池名称前缀
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        // 核心线程数量
        executor.setCorePoolSize(CPU_CORES);
        // 最大线程数量
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        // 队列最大长度
        executor.setQueueCapacity(QUEUE_CAPACITY);
        // 拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 最大空闲时间
        executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        executor.initialize();
        return executor;
    }


}
