package com.amusing.start.topic;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/10/30
 */
public class TopicConstant {

    // 交易创建，等待买家付款。
    public static final String ALI_PAY_NOTIFY_WAIT_TOPIC = "ALI_PAY_NOTIFY_WAIT_TOPIC";

    // 未付款交易超时关闭，或支付完成后全额退款。
    public static final String ALI_PAY_NOTIFY_CLOSED_TOPIC = "ALI_PAY_NOTIFY_CLOSED_TOPIC";

    // 交易支付成功。
    public static final String ALI_PAY_NOTIFY_SUCCESS_TOPIC = "ALI_PAY_NOTIFY_SUCCESS_TOPIC";

    // 交易结束，不可退款。
    public static final String ALI_PAY_NOTIFY_FINISHED_TOPIC = "ALI_PAY_NOTIFY_FINISHED_TOPIC";


}
