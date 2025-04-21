package com.amusing.start.order.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
/**
 * @author Lv.QingYu
 * @description: 订单商品关联关系表
 * @since 2021/10/10
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderProduct implements Serializable {

    private static final long serialVersionUID = 4016582381548236295L;

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
     * 单价ID
     */
    private String priceId;

    /**
     * 单价
     */
    private Integer price;

    /**
     * 商品数量
     */
    private Integer num;

    /**
     * 订单金额
     */
    private Integer amount;

}