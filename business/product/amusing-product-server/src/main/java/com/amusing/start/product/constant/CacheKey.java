package com.amusing.start.product.constant;

/**
 * Created by lvqingyu on 2022/10/15.
 */
public class CacheKey {

    private static final String SHOP_CAR_PREFIX = "shop_car:";

    public static String getShopCar(String userId) {
        return SHOP_CAR_PREFIX + userId;
    }

}
