package com.amusing.start.order.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @since 2021/10/10
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductResponse {
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 商铺ID
     */
    private String shopId;
    /**
     * 商品ID
     */
    private String productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 价格ID
     */
    private String priceId;
    /**
     * 单价
     */
    private Integer price;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 金额。单位：分
     */
    private Integer amount;
}
