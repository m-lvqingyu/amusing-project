package com.amusing.start.order.service;

import com.amusing.start.order.pojo.Order;
import com.amusing.start.order.req.AdminOrderPageReq;
import com.amusing.start.order.resp.AdminOrderPageResp;
import com.amusing.start.order.resp.OrderDetailResp;
import com.amusing.start.result.PageResult;

/**
 * @author Lv.QingYu
 * @since 2021/10/10
 */
public interface OrderService {

    Integer insert(Order order);

    /**
     * 订单列表分页
     *
     * @param req 分页查询参数
     * @return 订单信息
     */
    PageResult<AdminOrderPageResp> page(AdminOrderPageReq req);

    /**
     * @param orderNo 订单编号
     * @return 订单详情
     * @description: 获取订单详情
     */
    OrderDetailResp getOrderDetail(String orderNo);

    /**
     * @param orderNo 订单编号
     * @return 订单信息
     * @description: 根据订单编号，获取订单信息
     */
    Order getByNo(String orderNo);

}
