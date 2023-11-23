package com.amusing.start.order.mapper;

import com.amusing.start.order.entity.pojo.OrderAliRefundInfo;
import com.amusing.start.order.enums.OrderRefundEnum;

/**
 * @author Lv.QingYu
 * @description: 订单-支付宝退款记录表
 * @since 2023/10/2
 */
public interface OrderAliRefundInfoMapper {

    /**
     * @param orderPayInfo 退款信息
     * @return ID
     * @description: 保存支付宝退款记录
     */
    Integer insert(OrderAliRefundInfo orderPayInfo);

    /**
     * @param tradeNo      支付宝交易流水号
     * @param outRequestNo 退款流水号
     * @param status       状态 {@link OrderRefundEnum}
     * @return 更新数量
     * @description: 根据流水号，更新退款状态
     */
    Integer updateStatus(String tradeNo, String outRequestNo, Integer status);

}
