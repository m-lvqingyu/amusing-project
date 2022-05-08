package com.amusing.start.order.constant;

/**
 * @author ：lv.qingyu
 * @date ：2022/4/16 15:15
 */
public class OrderCacheKey {

    public static final String SHOP_CAR_PREFIX = "amusing_shop_car:";

    public static final Integer SHOP_CAR_CACHE_TIME_TO_LIVE = 10080;

    public static String getShopCarKey(String userId) {
        return SHOP_CAR_PREFIX + userId;
    }

}
