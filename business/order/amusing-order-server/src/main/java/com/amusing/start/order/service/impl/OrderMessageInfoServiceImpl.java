package com.amusing.start.order.service.impl;

import com.amusing.start.order.mapper.OrderMessageInfoMapper;
import com.amusing.start.order.pojo.OrderMessageInfo;
import com.amusing.start.order.service.IOrderMessageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create By 2021/11/13
 *
 * @author lvqingyu
 */
@Service
public class OrderMessageInfoServiceImpl implements IOrderMessageInfoService {

    private final OrderMessageInfoMapper createOrderMsgMapper;

    @Autowired
    private OrderMessageInfoServiceImpl(OrderMessageInfoMapper createOrderMsgMapper) {
        this.createOrderMsgMapper = createOrderMsgMapper;
    }

    @Override
    public int save(OrderMessageInfo createOrderMsg) {
        return createOrderMsgMapper.insertSelective(createOrderMsg);
    }

    @Override
    public OrderMessageInfo getCreateOrderMsgByOrderNo(String orderNo) {
        return createOrderMsgMapper.selectByOrderNo(orderNo);
    }

    @Override
    public int updateMsgStatus(String orderNo, Integer msgStatus) {
        return createOrderMsgMapper.updateMsgStatus(orderNo, msgStatus);
    }

}
