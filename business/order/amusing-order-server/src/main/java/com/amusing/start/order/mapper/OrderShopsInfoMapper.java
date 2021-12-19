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

    /**
     * 保存订单与商铺关联关系
     *
     * @param record
     * @return
     */
    int insertSelective(OrderShopsInfo record);

    /**
     * 更新订单与商铺关联关系
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(OrderShopsInfo record);

    /**
     * 批量新增订单与商铺关联关系
     *
     * @param shopsInfoList
     * @return
     */
    int batchInsertSelective(@Param("shopsInfoList") List<OrderShopsInfo> shopsInfoList);


}