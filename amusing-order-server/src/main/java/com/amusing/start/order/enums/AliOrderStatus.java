package com.amusing.start.order.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @since 2023/9/27
 */
@Getter
public enum AliOrderStatus {

    /**
     * 交易创建，等待买家付款
     */
    WAIT_BUYER_PAY,

    /**
     * 未付款交易超时关闭，或支付完成后全额退款(在指定时间段内未支付时关闭的交易或在交易完成全额退款成功时关闭的交易)
     */
    TRADE_CLOSED,

    /**
     * 交易支付成功，可退款(商家签约的产品支持退款功能的前提下，买家付款成功)
     */
    TRADE_SUCCESS,

    /**
     * 交易结束，不可退款(商家签约的产品不支持退款功能的前提下，买家付款成功。
     * 或者，商家签约的产品支持退款功能的前提下，交易已经成功并且已经超过可退款期限(默认12个月)。)
     */
    TRADE_FINISHED

}
