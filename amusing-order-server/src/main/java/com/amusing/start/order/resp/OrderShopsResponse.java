package com.amusing.start.order.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2021/10/10
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderShopsResponse {
    /**
     * 商铺ID
     */
    private String shopsId;
    /**
     * 商铺名称
     */
    private String shopsName;
    /**
     * 顺序
     */
    private Integer sort;
    /**
     * 商品信息
     */
    private List<OrderProductResponse> productResponseList;
}
