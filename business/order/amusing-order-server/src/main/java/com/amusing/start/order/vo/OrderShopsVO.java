package com.amusing.start.order.vo;

import lombok.Data;

import java.util.List;

/**
 * Create By 2021/10/10
 *
 * @author lvqingyu
 */
@Data
public class OrderShopsVO {

    /**
     * 商铺ID
     */
    private String shopsId;

    /**
     * 商铺名称
     */
    private String shopsName;

    private List<OrderProductVO> orderProductVOList;

}
