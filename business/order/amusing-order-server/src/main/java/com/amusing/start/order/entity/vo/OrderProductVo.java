package com.amusing.start.order.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @description: 订单-商品信息
 * @since 2021/10/10
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "订单-商品信息")
public class OrderProductVo {

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "商铺ID")
    private String shopId;

    @ApiModelProperty(value = "商品ID")
    private String productId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "单价ID")
    private String priceId;
    
    @ApiModelProperty(value = "单价")
    private Integer price;

    @ApiModelProperty(value = "商品数量")
    private Integer num;

    @ApiModelProperty(value = "订单金额")
    private Integer amount;

}
