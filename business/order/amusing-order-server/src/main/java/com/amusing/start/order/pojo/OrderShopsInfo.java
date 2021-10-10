package com.amusing.start.order.pojo;

import lombok.Data;

/**
 * @author 订单商铺信息
 */
@Data
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

}