package com.amusing.start.order.mapper;

import com.amusing.start.order.entity.pojo.OrderAliPayInfo;
import com.amusing.start.order.enums.OrderPayStatusEnum;
import org.apache.ibatis.annotations.Param;

/**
 * @author Lv.QingYu
 * @description: 订单-支付宝支付记录表
 * @since 2023/10/2
 */
public interface OrderAliPayInfoMapper {

    /**
     * @param orderPayInfo 支付信息
     * @return 新增数量
     * @description: 新增支付记录
     */
    Integer insert(OrderAliPayInfo orderPayInfo);

    /**
     * @param tradeNo 支付宝交易号
     * @return 支付记录
     * @description: 根据支付宝交易号，获取支付记录
     */
    OrderAliPayInfo getByTradeNo(@Param("tradeNo") String tradeNo);

    /**
     * @param tradeNo 支付宝交易号
     * @param status  状态 {@link OrderPayStatusEnum}
     * @return 更新数量
     * @description: 根据支付交易号，更新记录状态
     */
    Integer updateStatus(@Param("tradeNo") String tradeNo, @Param("status") Integer status);

    /**
     * @param payId ID
     * @return 支付记录
     * @description: 根据ID，获取支付记录
     */
    OrderAliPayInfo getById(@Param("payId") Long payId);

}