package com.amusing.start.order.service.impl;

import com.amusing.start.order.exception.OrderException;
import com.amusing.start.order.mapper.OrderInfoMapper;
import com.amusing.start.order.mapper.OrderProductInfoMapper;
import com.amusing.start.order.mapper.OrderShopsInfoMapper;
import com.amusing.start.order.pojo.OrderInfo;
import com.amusing.start.order.pojo.OrderProductInfo;
import com.amusing.start.order.pojo.OrderShopsInfo;
import com.amusing.start.order.service.IOrderService;
import com.amusing.start.order.vo.OrderDetailVO;
import com.amusing.start.order.vo.OrderProductVO;
import com.amusing.start.order.vo.OrderShopsVO;
import com.amusing.start.result.ApiCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By 2021/10/10
 *
 * @author lvqingyu
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    public OrderServiceImpl(OrderInfoMapper orderInfoMapper,
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
    public OrderDetailVO get(String orderId, String userId) throws OrderException {
        OrderInfo orderInfo = orderInfoMapper.selectOrderNoAndUserId(orderId, userId);
        if (orderInfo == null) {
            throw new OrderException(ApiCode.ORDER_NOT_FOUND);
        }

        OrderDetailVO orderDetail = new OrderDetailVO();
        setOrderInfoProper(orderInfo, orderDetail);

        String orderNo = orderInfo.getOrderNo();
        List<OrderShopsInfo> shopsInfoList = orderShopsInfoMapper.selectOrderNo(orderNo);
        List<OrderShopsVO> orderShopsVOList = new ArrayList<>();
        if (shopsInfoList == null || shopsInfoList.isEmpty()) {
            orderDetail.setOrderShopsVOList(orderShopsVOList);
            return orderDetail;
        }

        for (OrderShopsInfo orderShopsInfo : shopsInfoList) {
            String shopsId = orderShopsInfo.getShopsId();
            List<OrderProductInfo> productInfoList = orderProductInfoMapper.selectOrderNoAndShopsId(orderNo, shopsId);
            OrderShopsVO orderShopsVO = new OrderShopsVO();
            orderShopsVO.setShopsId(orderShopsInfo.getShopsId());
            List<OrderProductVO> orderProductVOList = new ArrayList<>();
            if (productInfoList != null && !productInfoList.isEmpty()) {
                productInfoList.stream().forEach(i -> {
                    OrderProductVO productVO = new OrderProductVO();
                    BeanUtils.copyProperties(i, productVO);
                    orderProductVOList.add(productVO);
                });
            }
            orderShopsVO.setOrderProductVOList(orderProductVOList);
            orderShopsVOList.add(orderShopsVO);
        }

        orderDetail.setOrderShopsVOList(orderShopsVOList);
        return orderDetail;
    }

    private void setOrderInfoProper(OrderInfo orderInfo, OrderDetailVO orderDetail) {
        BeanUtils.copyProperties(orderInfo, orderDetail);
    }


}
