package com.amusing.start.order.mapper;

import com.amusing.start.order.pojo.OrderShopsInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lv.qingyu
 */
@Mapper
public interface OrderShopsInfoMapper {

    /**
     * 根据订单编号获取商铺信息
     *
     * @param orderNo 订单编号
     * @return
     */
    List<OrderShopsInfo> selectOrderNo(@Param("orderNo") String orderNo);

    int insertSelective(OrderShopsInfo record);

    int updateByPrimaryKeySelective(OrderShopsInfo record);

}