package com.amusing.start.order.mapper;

import com.amusing.start.order.pojo.OrderMessageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMessageInfoMapper {

    int insertSelective(OrderMessageInfo record);

    OrderMessageInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderMessageInfo record);

    /**
     * 根据订单编号查询订单业务状态信息
     *
     * @param orderNo 订单编号
     * @return
     */
    OrderMessageInfo selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据订单编号，更新消息发送状态
     *
     * @param orderNo   订单编号
     * @param msgStatus 消息状态
     * @return
     */
    int updateMsgStatus(@Param("orderNo") String orderNo, @Param("msgStatus") Integer msgStatus);
}