package com.amusing.start.order.service;

import com.amusing.start.order.exception.OrderException;
import com.amusing.start.order.vo.OrderDetailVO;

/**
 * Create By 2021/10/10
 *
 * @author lvqingyu
 */
public interface IOrderService {

    /**
     * 根据订单ID和用户ID，获取订单详情
     *
     * @param orderId 订单ID
     * @param userId  用户ID
     * @return
     * @throws OrderException
     */
    OrderDetailVO get(String orderId, String userId) throws OrderException;

    /**
     * 判断订单是否已经取消
     *
     * @param orderNo 订单编号
     * @return
     */
    Boolean isCancel(String orderNo);

}
