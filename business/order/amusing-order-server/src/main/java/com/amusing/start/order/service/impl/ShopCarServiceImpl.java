package com.amusing.start.order.service.impl;

import com.amusing.start.order.constant.OrderConstant;
import com.amusing.start.order.constant.OrderCacheKey;
import com.amusing.start.order.service.IShopCarService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ：lvqingyu
 * @date ：2022/4/9 17:59
 */
@Slf4j
@Service
public class ShopCarServiceImpl implements IShopCarService {

    private final RedissonClient redissonClient;

    @Autowired
    public ShopCarServiceImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean operation(String userId, String productId, Integer productNum) {
        String shopCarKey = OrderCacheKey.getShopCarKey(userId);
        try {
            RMap<Object, Object> rMap = redissonClient.getMap(shopCarKey);
            rMap.expire(OrderCacheKey.SHOP_CAR_CACHE_TIME_TO_LIVE, TimeUnit.MINUTES);
            rMap.put(productId, productNum);
            return true;
        } catch (Exception e) {
            log.error("[ShopCar]-[add]-userId:{}, msg:{}", userId, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @Override
    public boolean del(String userId, String productId) {
        String shopCarKey = OrderCacheKey.getShopCarKey(userId);
        try {
            RMap<Object, Object> rMap = redissonClient.getMap(shopCarKey);
            rMap.remove(productId);
            int size = rMap.size();
            if (size <= OrderConstant.ZERO) {
                rMap.delete();
            }
            return true;
        } catch (Exception e) {
            log.error("[ShopCar]-[del]-userId:{}, msg:{}", userId, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @Override
    public Map<String, Integer> get(String userId) {
        String shopCarKey = OrderCacheKey.getShopCarKey(userId);
        Map<String, Integer> result = new HashMap<>();
        try {
            RMap<Object, Object> rMap = redissonClient.getMap(shopCarKey);
            for (Map.Entry<Object, Object> next : rMap.entrySet()) {
                String key = String.valueOf(next.getKey());
                Integer value = (Integer) next.getValue();
                result.put(key, value);
            }
            return result;
        } catch (Exception e) {
            log.error("[ShopCar]-[get]-userId:{}, msg:{}", userId, Throwables.getStackTraceAsString(e));
            return result;
        }
    }

}
