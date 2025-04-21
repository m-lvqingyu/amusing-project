package com.amusing.start.limit.config;

import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.limit.handler.RequestLimitHandler;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2024/8/6
 */
@Configuration
@ConditionalOnProperty(name = "spring.limit.enabled", havingValue = "true")
public class LimitConfig {

    @Value("${spring.limit.redis.redisson.mode}")
    private String mode;

    @Value("${spring.limit.redis.redisson.masterName:}")
    private String masterName;

    @Value("${spring.limit.redis.redisson.address}")
    private String address;

    @Value("${spring.limit.redis.redisson.password:}")
    private String password;

    @Value("${spring.limit.redis.redisson.database:0}")
    private Integer database;

    @Bean
    public RedissonClient redissonClient() {
        if (StringUtils.isBlank(password)) {
            password = null;
        }
        Config config = config();
        return Redisson.create(config);
    }

    @Bean("requestLimitHandler")
    public RequestLimitHandler requestLimitHandler(RedissonClient redissonClient) {
        return new RequestLimitHandler(redissonClient);
    }

    private static final String SINGLE = "single";

    private static final String CLUSTER = "cluster";

    private static final String SENTINEL = "sentinel";

    private static final String MASTER_SLAVE = "master-slave";

    private static final String COMMA = ",";

    private static final int ONE = 1;

    private static final int ZERO = 0;

    private Config config() {
        Config config = new Config();
        if (SINGLE.equalsIgnoreCase(mode)) {
            config.useSingleServer()
                    .setDatabase(database)
                    .setPassword(password)
                    .setAddress(address);
            return config;
        }
        if (CLUSTER.equalsIgnoreCase(mode)) {
            String[] clusterAddresses = address.split(COMMA);
            config.useClusterServers()
                    .setPassword(password)
                    .addNodeAddress(clusterAddresses);
            return config;
        }
        if (SENTINEL.equalsIgnoreCase(mode)) {
            String[] sentinelAddresses = address.split(COMMA);
            config.useSentinelServers()
                    .setDatabase(database)
                    .setPassword(password)
                    .setMasterName(masterName)
                    .addSentinelAddress(sentinelAddresses);
            return config;
        }
        if (MASTER_SLAVE.equals(mode)) {
            String[] masterSlaveAddresses = address.split(COMMA);
            if (masterSlaveAddresses.length == ONE) {
                throw new CustomException(CommunalCode.SERVICE_ERR);
            }
            String[] slaveAddresses = new String[masterSlaveAddresses.length - ONE];
            System.arraycopy(masterSlaveAddresses, ONE, slaveAddresses, ZERO, slaveAddresses.length);
            config.useMasterSlaveServers()
                    .setDatabase(database)
                    .setPassword(password)
                    .setMasterAddress(masterSlaveAddresses[ZERO])
                    .addSlaveAddress(slaveAddresses);
            return config;
        }
        throw new CustomException(CommunalCode.SERVICE_ERR);
    }

    //spring:
    //  redis:
    //    # redisson配置
    //    redisson:
    //        # 如果该值为false，系统将不会创建RedissionClient的bean。
    //        enabled: true
    //        # mode的可用值为，single/cluster/sentinel/master-slave
    //        mode: single
    //        # single: 单机模式
    //        #   address: redis://localhost:6379
    //        # cluster: 集群模式
    //        #   每个节点逗号分隔，同时每个节点前必须以redis://开头。
    //        #   address: redis://localhost:6379,redis://localhost:6378,...
    //        # sentinel:
    //        #   每个节点逗号分隔，同时每个节点前必须以redis://开头。
    //        #   address: redis://localhost:6379,redis://localhost:6378,...
    //        # master-slave:
    //        #   每个节点逗号分隔，第一个为主节点，其余为从节点。同时每个节点前必须以redis://开头。
    //        #   address: redis://localhost:6379,redis://localhost:6378,...
    //        address: redis://127.0.0.1:6001
    //        # redis 密码，空可以不填。
    //        password: redis@pass
    //        database: 0

}
