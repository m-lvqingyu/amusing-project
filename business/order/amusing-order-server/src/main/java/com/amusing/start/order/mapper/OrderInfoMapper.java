package com.amusing.start.order.mapper;

import com.amusing.start.order.pojo.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lv.qingyu
 */
@Mapper
public interface OrderInfoMapper {

    /**
     * 根据订单编号获取订单状态
     *
     * @param orderNo
     * @return
     */
    Integer selectOrderStatus(@Param("orderNo") String orderNo);

    /**
     * 根据订单编号和用户ID，获取用户ID
     *
     * @param orderNo 订单编号
     * @param userId  预定用户ID
     * @return
     */
    OrderInfo selectOrderNoAndUserId(@Param("orderNo") String orderNo, @Param("userId") String userId);

    /**
     * 保存订单信息
     *
     * @param record
     * @return
     */
    int insertSelective(OrderInfo record);

    /**
     * 更新订单信息
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(OrderInfo record);

    /**
     * 根据订单编号更新订单状态
     *
     * @param orderNo
     * @param status
     * @return
     */
    int updateOrderStatus(@Param("orderNo") String orderNo, @Param("status") int status);

}