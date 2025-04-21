package com.amusing.start.order.utils;

/**
 * @author Lv.QingYu
 * @since 2023/03/06
 */
public class OrderCacheUtil {

    /**
     * @param userId 用户ID
     * @param uri    请求PATH
     * @return 接口请求频次缓存KEY
     * @description: 接口频次缓存Key
     */
    public static String reqLimitKey(String userId, String uri) {
        return "rl_or:" + userId + ":" + uri;
    }

    /**
     * @param userId 用户ID
     * @return 缓存Key
     * @description: 订单创建锁
     */
    public static String createLock(String userId) {
        return "order:create:lock:" + userId;
    }

    /**
     * @param orderNo 订单编号
     * @return 缓存Key
     * @description: 订单状态变更锁
     */
    public static String orderStatusChangeLock(String orderNo) {
        return "order:status:change:lock:" + orderNo;
    }

}
