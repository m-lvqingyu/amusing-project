package com.amusing.start.platform.service.pay.ali;

import com.amusing.start.client.output.AliPayTradeOutput;

/**
 * @author Lv.QingYu
 * @description: 支付宝电脑网站支付API
 * @since 2023/10/17
 */
public interface PcPayService {

    /**
     * @param orderNo 订单编号
     * @return true：成功 false：失败
     * @description: 关闭交易（只有等待买家付款状态下才能发起交易关闭）
     */
    Boolean close(String orderNo);

    /**
     * @param tradeNo  支付宝交易号
     * @param amount   退款金额(单位分)
     * @param refundNo 退款编号
     * @return true：成功 false：失败
     * @description: 统一收单交易退款接口
     */
    Boolean refund(String tradeNo, Integer amount, String refundNo);

    /**
     * @param tradeNo  支付宝交易号
     * @param refundNo 退款编号
     * @return true：成功 false：失败
     * @description: 统一收单交易退款查询
     */
    Boolean refundQuery(String tradeNo, String refundNo);

    /**
     * @param orderNo 订单编号
     * @param tradeNo 支付宝交易号
     * @return 交易信息
     * @description: 统一收单交易查询
     */
    AliPayTradeOutput query(String orderNo, String tradeNo);

}
