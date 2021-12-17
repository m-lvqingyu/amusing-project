package com.amusing.start.order.service;

import com.amusing.start.order.dto.create.OrderCreateDto;
import com.amusing.start.order.exception.OrderException;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 创建订单相关接口
 * @date 2021/10/15 16:43
 */
public interface IOrderCreateService {

    /**
     * 创建订单
     *
     * @param orderCreateDto
     * @return
     * @throws OrderException
     */
    String create(OrderCreateDto orderCreateDto) throws OrderException;

}
