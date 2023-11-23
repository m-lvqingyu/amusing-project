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
 * @description: 订单-商铺信息
 * @since 2021/10/10
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "订单-商铺信息")
public class OrderShopsVo {

    @ApiModelProperty(value = "商铺ID")
    private String shopsId;

    @ApiModelProperty(value = "商铺名称")
    private String shopsName;

    @ApiModelProperty(value = "排列顺序")
    private Integer sort;

    @ApiModelProperty(value = "商品集合")
    private List<OrderProductVo> productVoList;

}
