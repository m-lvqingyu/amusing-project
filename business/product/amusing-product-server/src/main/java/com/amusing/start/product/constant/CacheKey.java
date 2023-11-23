package com.amusing.start.product.constant;

/**
 * @author Lv.QingYu
 * @description: 缓存KEY
 * @since 2022/10/15
 */
public class CacheKey {

    public static final Long SHOP_CAR_TIME_OUT = 7L;

    private static final String SHOP_CAR_PREFIX = "shop_car:";

    public static String getShopCar(String userId) {
        return SHOP_CAR_PREFIX + userId;
    }

}
