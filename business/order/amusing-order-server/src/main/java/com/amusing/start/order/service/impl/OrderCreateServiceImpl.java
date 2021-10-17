package com.amusing.start.order.service.impl;

import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.order.dto.OrderCreateDto;
import com.amusing.start.order.enums.OrderCode;
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
    public OrderCreateServiceImpl(UserClient userClient,
                                  OrderInfoMapper orderInfoMapper,
                                  OrderShopsInfoMapper orderShopsInfoMapper,
                                  OrderProductInfoMapper orderProductInfoMapper) {
        this.userClient = userClient;
        this.orderInfoMapper = orderInfoMapper;
        this.orderShopsInfoMapper = orderShopsInfoMapper;
        this.orderProductInfoMapper = orderProductInfoMapper;
    }

    private final UserClient userClient;
    private final OrderInfoMapper orderInfoMapper;
    private final OrderShopsInfoMapper orderShopsInfoMapper;
    private final OrderProductInfoMapper orderProductInfoMapper;


    @Override
    public String create(OrderCreateDto orderCreateDto) throws OrderException {
        String reserveUserId = orderCreateDto.getReserveUserId();
        UserAccountOutput userAccountOutput = userClient.account(reserveUserId);
        if(userAccountOutput == null){
            throw new OrderException(OrderCode.USER_NOT_FOUND);
        }


        return null;
    }
}
