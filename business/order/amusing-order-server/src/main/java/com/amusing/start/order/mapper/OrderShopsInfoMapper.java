package com.amusing.start.order.mapper;

import com.amusing.start.order.entity.pojo.OrderShopsInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 订单与商铺关联关系Mapper
 * @since 2021/10/10
 */
@Mapper
public interface OrderShopsInfoMapper {

    /**
     * @param orderShopsInfo 订单-商铺信息
     * @return 关联关系ID
     * @description: 保存订单-商铺关联关系
     */
    Integer insert(OrderShopsInfo orderShopsInfo);

    /**
     * @param orderNo 订单编号
     * @return 订单-商铺关系集合
     * @description: 根据订单编号，获取订单-商铺关系集合
     */
    List<OrderShopsInfo> getByNo(String orderNo);

}