package com.amusing.start.order.mapper;

import com.amusing.start.order.pojo.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Lv.QingYu
 * @since 2021/10/10
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}