package com.amusing.start.order.mapper;

import com.amusing.start.order.entity.pojo.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lv.qingyu
 */
@Mapper
public interface OrderInfoMapper {

    /**
     * 保存订单信息
     *
     * @param orderInfo 订单信息
     * @return
     */
    int insert(OrderInfo orderInfo);

    /**
     * 更新订单信息
     *
     * @param orderInfo 订单信息
     * @return
     */
    int update(OrderInfo orderInfo);

    /**
     * 订单详情
     *
     * @param userId  用户ID
     * @param orderNo 订单编号
     * @return
     */
    OrderInfo getByNo(String userId, String orderNo);

}