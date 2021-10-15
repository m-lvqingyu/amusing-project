package com.amusing.start.order.service.impl;

import com.amusing.start.order.dto.OrderCreateDto;
import com.amusing.start.order.exception.OrderException;
import com.amusing.start.order.mapper.OrderInfoMapper;
import com.amusing.start.order.mapper.OrderProductInfoMapper;
import com.amusing.start.order.mapper.OrderShopsInfoMapper;
import com.amusing.start.order.service.IOrderCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2021/10/15 16:44
 */
@Service
public class OrderCreateServiceImpl implements IOrderCreateService {

    @Autowired
    public OrderCreateServiceImpl(OrderInfoMapper orderInfoMapper,
                                  OrderShopsInfoMapper orderShopsInfoMapper,
                                  OrderProductInfoMapper orderProductInfoMapper) {
        this.orderInfoMapper = orderInfoMapper;
        this.orderShopsInfoMapper = orderShopsInfoMapper;
        this.orderProductInfoMapper = orderProductInfoMapper;
    }

    private OrderInfoMapper orderInfoMapper;

    private OrderShopsInfoMapper orderShopsInfoMapper;

    private OrderProductInfoMapper orderProductInfoMapper;

    @Override
    public String create(OrderCreateDto orderCreateDto) throws OrderException {
        return null;
    }
}
