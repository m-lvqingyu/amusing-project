package com.amusing.start.order.service;

import com.amusing.start.exception.CustomException;
import com.amusing.start.order.entity.dto.CreateDto;
import com.amusing.start.order.entity.vo.OrderDetailVo;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 创建订单相关接口
 * @date 2021/10/15 16:43
 */
public interface IOrderService {

    /**
     * 创建订单
     *
     * @param createDto
     * @return
     * @throws CustomException
     */
    String create(String userId, CreateDto createDto) throws CustomException;

    OrderDetailVo getOrderDetail(String userId, String orderNo) throws CustomException;

}
