package com.amusing.start.platform.constant;

import com.amusing.start.constant.CommConstant;

/**
 * @author Lv.QingYu
 * @description: 缓存KEY
 * @since 2022/10/15
 */
public class CacheKey {

    public static String getDingTalkAccessTokenKey() {
        return "platform:dt:at";
    }

    public static long getDingTalkAccessTokenExpireIn(long expireIn) {
        return expireIn - CommConstant.ONE_HUNDRED;
    }

    public static String getAliPcPayOrderKey(String orderNo) {
        return "order:ali:pc:info:" + orderNo;
    }

    public static String getOrderNotifyLockKey(String notifyId) {
        return "order:ali:nf:lc:" + notifyId;
    }

}
