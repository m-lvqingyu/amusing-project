package com.amusing.start.platform.service.pay.ali;

/**
 * @author Lv.QingYu
 * @description: 支付宝支付回调接口
 * @since 2023/10/30
 */
public interface PayNotifyService {

    /**
     * @param notifyId       通知ID
     * @param buyerLogonId   买家支付宝账户ID
     * @param orderNo        订单号
     * @param tradeNo        支付宝交易号，支付宝交易凭证号。
     * @param tradeStatus    交易状态
     * @param totalAmount    订单金额
     * @param receiptAmount  实收金额
     * @param invoiceAmount  开票金额
     * @param buyerPayAmount 用户在交易中支付的金额
     * @param pointAmount    使用集分宝支付金额
     * @param gmtPayment     交易付款时间
     * @return 出来结果
     * @description: 电脑网站支付-异步回调（支付成功-支付成功触发）
     */
    String pcAsyncNotify(String notifyId,
                         String buyerLogonId,
                         String orderNo,
                         String tradeNo,
                         String tradeStatus,
                         String totalAmount,
                         String receiptAmount,
                         String invoiceAmount,
                         String buyerPayAmount,
                         String pointAmount,
                         String gmtPayment);

    Boolean updateAsyncNotifyStatus(Long id, Integer status);

}
