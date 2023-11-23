package com.amusing.start.order.mapper;

import com.amusing.start.order.entity.pojo.OrderProductInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 订单商品关联关系Mapper
 * @since 2021/10/10
 */
@Mapper
public interface OrderProductInfoMapper {

    /**
     * @param record 订单与商品关联信息
     * @return 关联关系ID
     * @description: 保存订单与商品关联关系
     */
    int insert(OrderProductInfo record);

    /**
     * @param orderNo 订单编号
     * @return 订单-商品关联关系集合
     * @description: 根据订单编号，获取订单-商品关联关系集合
     */
    List<OrderProductInfo> getByNo(String orderNo);

}