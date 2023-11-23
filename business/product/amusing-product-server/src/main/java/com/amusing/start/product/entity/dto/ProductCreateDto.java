package com.amusing.start.product.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Lv.QingYu
 * @description: 新增商品请求参数
 * @since 2021/12/24
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "新增商品请求参数")
public class ProductCreateDto {

    @ApiModelProperty(value = "商铺ID")
    @NotBlank(message = "商铺ID不能为空")
    private String shopId;

    @ApiModelProperty(value = "商品名称")
    @NotBlank(message = "商品名称不能为空")
    private String name;

    @ApiModelProperty(value = "商品数量")
    @NotNull(message = "商品数量不能为空")
    private Integer stock;

    @ApiModelProperty(value = "商品单价")
    @NotNull(message = "商品单价不能为空")
    private Integer price;

    @ApiModelProperty(value = "商品描述")
    private String describe;

}
