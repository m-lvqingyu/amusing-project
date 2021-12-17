package com.amusing.start.order.service.impl;

import com.amusing.start.order.enums.OrderCode;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private final OrderInfoMapper orderInfoMapper;

    private final OrderShopsInfoMapper orderShopsInfoMapper;

    private final OrderProductInfoMapper orderProductInfoMapper;

    @Override
    public OrderDetailVO get(String orderId, String userId) throws OrderException {
        // 查询订单基础信息
        OrderInfo orderInfo = orderInfoMapper.selectOrderNoAndUserId(orderId, userId);
        Optional.ofNullable(orderInfo).orElseThrow(() -> new OrderException(OrderCode.ORDER_NOT_FOUND));

        OrderDetailVO orderDetail = new OrderDetailVO();
        BeanUtils.copyProperties(orderInfo, orderDetail);

        String orderNo = orderInfo.getOrderNo();

        // 查询订单-商铺关联关系信息
        List<OrderShopsInfo> shopsInfoList = orderShopsInfoMapper.selectOrderNo(orderNo);
        List<OrderShopsVO> orderShopsVOList = new ArrayList<>();

        Optional.ofNullable(shopsInfoList).ifPresent(i -> {
            i.forEach(x -> {
                String shopsId = x.getShopsId();
                OrderShopsVO orderShopsVO = new OrderShopsVO();
                orderShopsVO.setShopsId(shopsId);
                orderShopsVO.setShopsName(x.getShopsName());

                List<OrderProductVO> orderProductVOList = new ArrayList<>();
                List<OrderProductInfo> productInfoList = orderProductInfoMapper.selectOrderNoAndShopsId(orderNo, shopsId);
                Optional.ofNullable(productInfoList).ifPresent(c -> {
                    c.forEach(z -> {
                        OrderProductVO productVO = new OrderProductVO();
                        BeanUtils.copyProperties(z, productVO);
                        orderProductVOList.add(productVO);
                    });
                });
                orderShopsVO.setProductVOList(orderProductVOList);
                orderShopsVOList.add(orderShopsVO);
            });
        });
        orderDetail.setOrderShopsVOList(orderShopsVOList);
        return orderDetail;
    }

}
