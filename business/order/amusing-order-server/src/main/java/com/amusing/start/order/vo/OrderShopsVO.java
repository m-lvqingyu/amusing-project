package com.amusing.start.order.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Create By 2021/10/10
 *
 * @author lvqingyu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderShopsVO {

    /**
     * 商铺ID
     */
    private String shopsId;

    /**
     * 商铺名称
     */
    private String shopsName;

    /**
     * 商品信息集合
     */
    private List<OrderProductVO> productVOList;

}
