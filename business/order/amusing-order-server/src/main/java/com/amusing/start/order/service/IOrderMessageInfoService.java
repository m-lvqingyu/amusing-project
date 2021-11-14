package com.amusing.start.order.service;

import com.amusing.start.order.pojo.OrderMessageInfo;

/**
 * Create By 2021/11/13
 *
 * @author lvqingyu
 */
public interface IOrderMessageInfoService {

    /**
     * 保存订单业务状态信息
     *
     * @param createOrderMsg
     * @return
     */
    int save(OrderMessageInfo createOrderMsg);

    /**
     * 根据订单编号查询订单业务状态信息
     *
     * @param orderNo 订单编号
     * @return
     */
    OrderMessageInfo getCreateOrderMsgByOrderNo(String orderNo);

    /**
     * 根据订单编号，更新消息发送状态
     *
     * @param orderNo   订单编号
     * @param msgStatus 消息状态
     * @return
     */
    int updateMsgStatus(String orderNo, Integer msgStatus);

}
