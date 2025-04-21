package com.amusing.start.user.utils;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2024/3/12
 */
@RequiredArgsConstructor
@Component
public class BloomFilterUtils {

    private final RedissonClient redissonClient;
    
    /**
     * 创建布隆过滤器
     *
     * @param filterName         过滤器名称
     * @param expectedInsertions 预测插入数量
     * @param falseProbability   误判率
     * @return 过滤器
     */
    public <T> RBloomFilter<T> create(String filterName, long expectedInsertions, double falseProbability) {
        RBloomFilter<T> bloomFilter = redissonClient.getBloomFilter(filterName);
        bloomFilter.tryInit(expectedInsertions, falseProbability);
        return bloomFilter;
    }


}
