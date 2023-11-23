package com.amusing.start.order.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 订单业务层
 * @since 2021/10/10
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "订单详情")
public class OrderDetailVo {

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "预定人ID")
    private String reserveId;

    @ApiModelProperty(value = "收件人ID")
    private String consigneeId;

    @ApiModelProperty(value = "订单总金额")
    private Integer totalAmount;

    @ApiModelProperty(value = "运费")
    private Integer freightAmount;

    @ApiModelProperty(value = "优惠券减免总金额")
    private Integer couponAmount;

    @ApiModelProperty(value = "活动减免总金额")
    private Integer activityAmount;

    @ApiModelProperty(value = "订单状态")
    private Integer status;

    @ApiModelProperty(value = "是否包邮")
    private Integer isFreight;

    @ApiModelProperty(value = "已评价")
    private Integer isEvaluate;

    @ApiModelProperty(value = "商品信息")
    private List<OrderShopsVo> shopsVoList;

}
