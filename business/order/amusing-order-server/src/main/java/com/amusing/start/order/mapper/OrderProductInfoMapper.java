package com.amusing.start.order.mapper;

import com.amusing.start.order.entity.pojo.OrderProductInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderProductInfoMapper {

    /**
     * 保存订单与商品关联关系
     *
     * @param record
     * @return
     */
    int insert(OrderProductInfo record);

    /**
     * 更新订单与商品关联关系
     *
     * @param record
     * @return
     */
    int update(OrderProductInfo record);

    List<OrderProductInfo> getByNo(String orderNo);


}