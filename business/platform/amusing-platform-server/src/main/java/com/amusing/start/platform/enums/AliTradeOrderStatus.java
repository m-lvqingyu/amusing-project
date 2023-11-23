package com.amusing.start.platform.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Lv.QingYu
 * @description: 支付宝订单状态 {@link <a href="https://opendocs.alipay.com/support/01raw9">...</a>}
 * @since 2023/9/27
 */
@Getter
public enum AliTradeOrderStatus {

    /**
     * 商家必须根据支付宝不同类型的业务通知，正确进行不同的业务处理，并且过滤重复的通知结果数据。
     * 在支付宝的业务通知中，只有交易通知状态为 TRADE_SUCCESS 或 TRADE_FINISHED 时，支付宝才会认定为买家付款成功。
     * 另外如果签约的产品支持退款，并且对应的产品默认支持能收到 TRADE_SUCCESS 或 TRADE_FINISHED 状态，该笔会先收到 TRADE_SUCCESS 交易状态，然后超过 交易有效退款时间 该笔交易会再次收到 TRADE_FINISHED 状态，实际该笔交易只支付了一次，切勿认为该笔交易支付两次。
     */

    // 交易创建，等待买家付款
    WAIT_BUYER_PAY,

    // 未付款交易超时关闭，或支付完成后全额退款(在指定时间段内未支付时关闭的交易或在交易完成全额退款成功时关闭的交易)
    TRADE_CLOSED,

    // 交易支付成功，可退款(商家签约的产品支持退款功能的前提下，买家付款成功)
    TRADE_SUCCESS,

    // 交易结束，不可退款(商家签约的产品不支持退款功能的前提下，买家付款成功。或者，商家签约的产品支持退款功能的前提下，交易已经成功并且已经超过可退款期限(默认12个月)。)
    TRADE_FINISHED;


    public static AliTradeOrderStatus getByName(String status) {
        if (StringUtils.isBlank(status)) {
            return null;
        }
        AliTradeOrderStatus[] values = AliTradeOrderStatus.values();
        for (AliTradeOrderStatus value : values) {
            if (value.name().equalsIgnoreCase(status)) {
                return value;
            }
        }
        return null;
    }

}
