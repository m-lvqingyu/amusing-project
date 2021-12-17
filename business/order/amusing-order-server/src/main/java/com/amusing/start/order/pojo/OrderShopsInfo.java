package com.amusing.start.order.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * @author 订单商铺信息
 */
@Data
@Builder
public class OrderShopsInfo {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 商铺ID
     */
    private String shopsId;

    /**
     * 商铺名称
     */
    private String shopsName;

}