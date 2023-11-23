package com.amusing.start.platform.constant;

import com.alibaba.fastjson.JSONObject;
import com.amusing.start.constant.CommConstant;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Lv.QingYu
 * @description: 支付宝常量
 * @since 2023/9/25
 */
public class AliPayConstant {

    /**
     * 订单编号
     */
    public static final String OUT_TRADE_NO = "out_trade_no";
    /**
     * 通知ID
     */
    public static final String NOTIFY_ID = "notify_id";
    /**
     * 订单金额。本次交易支付订单金额，单位为人民币（元），精确到小数点后 2 位
     */
    public static final String TOTAL_AMOUNT = "total_amount";
    /**
     * 实收金额。商家在交易中实际收到的款项，单位为人民币（元），精确到小数点后 2 位
     */
    public static final String RECEIPT_AMOUNT = "receipt_amount";
    /**
     * 开票金额。用户在交易中支付的可开发票的金额，单位为人民币（元），精确到小数点后 2 位
     */
    public static final String INVOICE_AMOUNT = "invoice_amount";
    /**
     * 用户在交易中支付的金额，单位为人民币（元），精确到小数点后 2 位
     */
    public static final String BUYER_PAY_AMOUNT = "buyer_pay_amount";
    /**
     * 使用集分宝支付金额，单位为人民币（元），精确到小数点后 2 位
     */
    public static final String POINT_AMOUNT = "point_amount";
    /**
     * 总退款金额。退款通知中，返回总退款金额，单位为人民币（元），精确到小数点后 2 位
     */
    public static final String REFUND_FEE = "refund_fee";
    /**
     * 订单标题/商品标题/交易标题/订单关键字等，是请求时对应参数，会在通知中原样传回
     */
    public static final String SUBJECT = "subject";
    /**
     * 交易创建时间。格式为 yyyy-MM-dd HH:mm:ss
     */
    public static final String GMT_CREATE = "gmt_create";
    /**
     * 交易付款时间。格式为 yyyy-MM-dd HH:mm:ss
     */
    public static final String GMT_PAYMENT = "gmt_payment";
    /**
     * 交易退款时间。格式为 yyyy-MM-dd HH:mm:ss.S
     */
    public static final String GMT_REFUND = "gmt_refund";
    /**
     * 交易结束时间。格式为 yyyy-MM-dd HH:mm:ss
     */
    public static final String GMT_CLOSE = "gmt_close";


    public static final String PRODUCT_CODE = "product_code";

    public static final String FAST_INSTANT_TRADE_PAY = "FAST_INSTANT_TRADE_PAY";

    public static final String TRADE_NO = "trade_no";

    public static final String REFUND_AMOUNT = "refund_amount";

    public static final String OUT_REQUEST_NO = "out_request_no";

    public static final String SCENE = "scene";

    public static final String BAR_CODE = "bar_code";

    public static final String AUTH_CODE = "auth_code";

    public static final String TRADE_STATUS = "trade_status";

    public static final String SUCCESS = "success";

    public static final String FAIL = "fail";

    public static final String TIME_EXPIRE = "time_expire";

    public static final String SIGN = "sign";

    /**
     * 退款成功常量
     */
    public static final String REFUND_SUCCESS = "REFUND_SUCCESS";

    public static final String SUBJECT_CONTENT = "云服务包年/包月订单";

    public static final String Y = "Y";

    public static final String BUYER_ID = "buyer_id";

    public static final String TIMEOUT_EXPRESS = "timeout_express";


    public static JSONObject buildCommPayRq(String orderNo, Integer amount, String subject) {
        JSONObject bizContent = new JSONObject();
        bizContent.put(AliPayConstant.OUT_TRADE_NO, orderNo);
        //支付金额，最小值0.01元
        String payAmount = new BigDecimal(amount).divide(
                        new BigDecimal(CommConstant.ONE_HUNDRED),
                        CommConstant.TWO,
                        RoundingMode.HALF_UP)
                .toPlainString();
        bizContent.put(AliPayConstant.TOTAL_AMOUNT, payAmount);
        //订单标题，不可使用特殊符号
        bizContent.put(AliPayConstant.SUBJECT, subject);
        return bizContent;
    }

}
