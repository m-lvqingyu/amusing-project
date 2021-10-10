package com.amusing.start.order.mapper;

import com.amusing.start.order.pojo.OrderProductInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderProductInfoMapper {

    /**
     * 根据订单编号和商铺ID获取商品信息
     *
     * @param orderNo
     * @param shopsId
     * @return
     */
    List<OrderProductInfo> selectOrderNoAndShopsId(@Param("orderNo") String orderNo, @Param("shopsId") String shopsId);

    int insertSelective(OrderProductInfo record);

    int updateByPrimaryKeySelective(OrderProductInfo record);

}