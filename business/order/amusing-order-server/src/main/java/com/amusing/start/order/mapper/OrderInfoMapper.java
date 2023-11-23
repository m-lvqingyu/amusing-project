package com.amusing.start.order.mapper;

import com.amusing.start.order.entity.pojo.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Lv.QingYu
 * @description: 订单Mapper
 * @since 2021/10/10
 */
@Mapper
public interface OrderInfoMapper {

    /**
     * @param orderInfo 订单信息
     * @return 订单ID
     * @description: 保存订单信息
     */
    Integer insert(OrderInfo orderInfo);

    /**
     * @param userId  用户ID
     * @param orderNo 订单编号
     * @return 订单信息
     * @description: 根据订单编号获取订单信息
     */
    OrderInfo getByNo(@Param("userId") String userId, @Param("orderNo") String orderNo);

    /**
     * @param orderNo 订单编号
     * @param status  订单状态
     * @param payId   支付ID
     * @return 更新条数
     * @description: 根据订单编号，更新订单状态
     */
    Integer updateStatus(@Param("orderNo") String orderNo, @Param("status") Integer status, @Param("payId") Long payId);

    /**
     * @param orderNo 订单编号
     * @param status  退款状态
     * @return 更新数量
     * @description: 根据订单编号更新退款状态
     */
    Integer updateCanRefund(@Param("orderNo") String orderNo, @Param("status") Integer status);

    /**
     * @param orderNo      订单编号
     * @param refundAmount 退款金额
     * @return 更新数量
     * @description: 根据订单编号，更新退款金额
     */
    Integer updateRefundAmount(@Param("orderNo") String orderNo, @Param("refundAmount") Integer refundAmount);

}