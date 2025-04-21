package com.amusing.start.user.config;

import com.amusing.start.user.utils.BloomFilterUtils;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lv.QingYu
 * @since 2023/12/15
 */
@RequiredArgsConstructor
@Configuration
public class BloomFilterConfig {

    private final BloomFilterUtils bloomFilterUtils;

    public static final int TEN_MILLION = 10000000;

    public static final double PROBABILITY = 0.01D;

    public static final String NAME_BLOOM_FILTER = "nameBloomFilter";

    public static final String PHONE_BLOOM_FILTER = "phoneBloomFilter";

    /**
     * @return 用户名布隆过滤器
     */
    @Bean(name = "nameBloomFilter")
    public RBloomFilter<String> nameBloomFilter() {
        return bloomFilterUtils.create(NAME_BLOOM_FILTER, TEN_MILLION, PROBABILITY);
    }

    /**
     * @return 手机号码布隆过滤器
     */
    @Bean(name = "phoneBloomFilter")
    public RBloomFilter<String> phoneBloomFilter() {
        return bloomFilterUtils.create(PHONE_BLOOM_FILTER, TEN_MILLION, PROBABILITY);
    }
}
