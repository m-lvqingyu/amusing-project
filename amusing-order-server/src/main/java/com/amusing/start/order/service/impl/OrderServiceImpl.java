package com.amusing.start.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.client.api.UserFeignClient;
import com.amusing.start.client.response.UserDetailResp;
import com.amusing.start.exception.CustomException;
import com.amusing.start.order.enums.OrderErrorCode;
import com.amusing.start.order.mapper.OrderMapper;
import com.amusing.start.order.mapper.OrderProductMapper;
import com.amusing.start.order.mapper.OrderShopsMapper;
import com.amusing.start.order.pojo.Order;
import com.amusing.start.order.pojo.OrderProduct;
import com.amusing.start.order.pojo.OrderShops;
import com.amusing.start.order.req.AdminOrderPageReq;
import com.amusing.start.order.resp.AdminOrderPageResp;
import com.amusing.start.order.resp.OrderDetailResp;
import com.amusing.start.order.resp.OrderProductResponse;
import com.amusing.start.order.resp.OrderShopsResponse;
import com.amusing.start.order.service.OrderService;
import com.amusing.start.result.ApiResult;
import com.amusing.start.result.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2021/10/10
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderInfoMapper;

    @Resource
    private OrderShopsMapper orderShopsInfoMapper;

    @Resource
    private OrderProductMapper orderProductInfoMapper;

    private final UserFeignClient userFeignClient;

    @Override
    public Integer insert(Order order) {
        return orderInfoMapper.insert(order);
    }

    @Override
    public PageResult<AdminOrderPageResp> page(AdminOrderPageReq req) {
        LambdaQueryWrapper<Order> query = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(req.getOrderNo())) {
            query.eq(Order::getOrderNo, req.getOrderNo());
        }
        Page<Order> page = Page.of(req.getPageNum(), req.getPageSize());
        Page<Order> orderPage = orderInfoMapper.selectPage(page, query);
        List<Order> orderInfoList = orderPage.getRecords();
        if (CollectionUtil.isEmpty(orderInfoList)) {
            return new PageResult<>();
        }
        List<AdminOrderPageResp> respList = new ArrayList<>();
        for (Order orderInfo : orderInfoList) {
            String userId = orderInfo.getUserId();
            ApiResult<UserDetailResp> detail = userFeignClient.detail(userId);
            String userName = "已注销账户~~~~~~";
            if (!detail.isSuccess()) {
                UserDetailResp userDetailResp = detail.getData();
                if (userDetailResp != null) {
                    userName = userDetailResp.getName();
                }
            }
            respList.add(new AdminOrderPageResp()
                    .setOrderNo(orderInfo.getOrderNo()).setUserId(userId)
                    .setName(userName)
                    .setAddress(orderInfo.getAddress())
                    .setTotalAmount(orderInfo.getTotalAmount())
                    .setRealAmount(orderInfo.getRealAmount())
                    .setReductionAmount(orderInfo.getReductionAmount())
                    .setStatus(orderInfo.getStatus()));
        }
        return new PageResult<>(orderPage.getPages(), orderPage.getTotal(), req.getPageSize(), req.getPageNum(), respList);
    }

    @Override
    public OrderDetailResp getOrderDetail(String orderNo) {
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getOrderNo, orderNo);
        Order orderInfo = orderInfoMapper.selectOne(orderWrapper);
        if (orderInfo == null) {
            throw new CustomException(OrderErrorCode.ORDER_NOT_FOUND);
        }
        LambdaQueryWrapper<OrderShops> orderShopsWrapper = new LambdaQueryWrapper<>();
        orderShopsWrapper.eq(OrderShops::getOrderNo, orderNo);
        List<OrderShops> shopsInfoList = orderShopsInfoMapper.selectList(orderShopsWrapper);
        if (CollectionUtil.isEmpty(shopsInfoList)) {
            throw new CustomException(OrderErrorCode.ORDER_NOT_FOUND);
        }
        LambdaQueryWrapper<OrderProduct> orderProductWrapper = new LambdaQueryWrapper<>();
        orderProductWrapper.eq(OrderProduct::getOrderNo, orderNo);
        List<OrderProduct> productInfoList = orderProductInfoMapper.selectList(orderProductWrapper);
        if (CollectionUtil.isEmpty(productInfoList)) {
            throw new CustomException(OrderErrorCode.ORDER_NOT_FOUND);
        }
        List<OrderShopsResponse> shopsResponseList = new ArrayList<>();
        shopsInfoList.forEach(i -> {
            String shopsId = i.getShopsId();
            List<OrderProductResponse> orderProductResponseList = new ArrayList<>();
            productInfoList.forEach(j -> {
                if (shopsId.equals(j.getShopId())) {
                    orderProductResponseList.add(BeanUtil.copyProperties(j, OrderProductResponse.class));
                }
            });
            shopsResponseList.add(new OrderShopsResponse()
                    .setShopsId(i.getShopsId())
                    .setShopsName(i.getShopsName())
                    .setSort(i.getSort())
                    .setProductResponseList(orderProductResponseList));
        });
        return new OrderDetailResp()
                .setOrderNo(orderNo)
                .setTotalAmount(orderInfo.getTotalAmount())
                .setStatus(orderInfo.getStatus())
                .setShopsResponseList(shopsResponseList);
    }

    @Override
    public Order getByNo(String orderNo) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo);
        return orderInfoMapper.selectOne(wrapper);
    }

}
