package com.amusing.start.cache.local;

import com.amusing.start.cache.constant.LocalCacheConstant;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * 基于谷歌 Cache 实现
 * <p>
 * Create By 2021/8/7
 *
 * @author lvqingyu
 */
@Slf4j
public class LocalCache<K, V> {

    private LoadingCache<K, V> cache;

    private InitLocalData<K, V> initLocalData;

    public LocalCache(InitLocalData<K, V> initLocalData) {
        this.initLocalData = initLocalData;
    }

    @PostConstruct
    public void init() {
        ExecutorService pool = initExecutorService();
        cache = CacheBuilder.newBuilder()
                .initialCapacity(LocalCacheConstant.INITIAL_CAPACITY)
                .concurrencyLevel(LocalCacheConstant.CONCURRENCY_LEVEL)
                .maximumSize(LocalCacheConstant.MAXIMUM_SIZE)
                .expireAfterAccess(LocalCacheConstant.EXPIRE_AFTER_ACCESS, LocalCacheConstant.TIME_UNIT)
                .refreshAfterWrite(LocalCacheConstant.REFRESH_AFTER_WRITE, LocalCacheConstant.TIME_UNIT)
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K k) {
                        return initLocalData.initData(k);
                    }

                    @Override
                    public ListenableFuture<V> reload(K key, V oldValue) {
                        ListenableFutureTask<V> task = ListenableFutureTask.create(new Callable<V>() {
                            @Override
                            public V call() {
                                return initLocalData.resetInitData(key);
                            }
                        });
                        pool.execute(task);
                        return task;
                    }
                });
    }

    /**
     * 初始化线程池
     *
     * @return
     */
    private ExecutorService initExecutorService() {
        ExecutorService pool = new ThreadPoolExecutor(
                LocalCacheConstant.CORE_POOL_SIZE,
                LocalCacheConstant.CONCURRENCY_LEVEL,
                LocalCacheConstant.KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(LocalCacheConstant.QUEUE_SIZE),
                new ThreadFactoryBuilder().setNameFormat("LocalCache-pool-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());
        return pool;
    }

    /**
     * 本地缓存数据
     *
     * @param k
     * @param v
     */
    public void setData(K k, V v) {
        cache.put(k, v);
    }

    /**
     * 从本地缓存中获取数据
     *
     * @param k
     * @return
     * @throws ExecutionException
     */
    public V getData(K k) throws ExecutionException {
        V v = cache.get(k);
        return v;
    }

}
