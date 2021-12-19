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

    /**
     * 保存订单与商品关联关系
     *
     * @param record
     * @return
     */
    int insertSelective(OrderProductInfo record);

    /**
     * 更新订单与商品关联关系
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(OrderProductInfo record);

    /**
     * 批量新增订单与商铺关联关系
     *
     * @param productInfoList
     * @return
     */
    int batchInsertSelective(@Param("productInfoList") List<OrderProductInfo> productInfoList);

}