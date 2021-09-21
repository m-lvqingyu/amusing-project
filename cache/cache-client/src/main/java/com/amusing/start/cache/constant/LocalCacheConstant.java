package com.amusing.start.cache.constant;

import java.util.concurrent.TimeUnit;

/**
 * Create By 2021/8/7
 *
 * @author lvqingyu
 */
public class LocalCacheConstant {

    /**
     * 缓存容器的初始容量
     */
    public static final int INITIAL_CAPACITY = 128;

    /**
     * 并发等级（根据cpu情况设置  推荐内核数*2）
     */
    public static final int CONCURRENCY_LEVEL = Runtime.getRuntime().availableProcessors();

    /**
     * 最大缓存条数。子类在构造方法中调用setMaxiMunSize(int size)来更改
     */
    public static final int MAXIMUM_SIZE = 1000;

    /**
     * 当缓存项在指定的时间段内没有被读或写就会被回收
     */
    public static final int EXPIRE_AFTER_ACCESS = 20;

    /**
     * 当缓存项上一次更新操作之后的多久会被刷新
     */
    public static final int REFRESH_AFTER_WRITE = 5;

    /**
     * 时间单位（分钟）
     */
    public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    /**
     * 线程池核心数
     */
    public static final int CORE_POOL_SIZE = 1;

    /**
     * 线程空闲时间
     */
    public static final int KEEP_ALIVE_TIME = 0;

    /**
     * 队列大小
     */
    public static final int QUEUE_SIZE = 128;
}
