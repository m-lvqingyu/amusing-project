package com.amusing.start.pay.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @since 2024/8/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AliPayH5QueryGoodResponse {
    /**
     * 商品ID
     */
    private String goodsId;
    /**
     * 支付宝定义的统一商品ID
     */
    private String alipayGoodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品数量
     */
    private Long quantity;
    /**
     * 商品单价。单位:元
     */
    private String price;
    /**
     * 商品描述
     */
    private String body;
}
