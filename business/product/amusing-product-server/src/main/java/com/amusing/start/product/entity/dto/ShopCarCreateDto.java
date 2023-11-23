package com.amusing.start.product.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Lv.QingYu
 * @description: 新增购物车请求参数
 * @since 2023/9/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(description = "新增购物车请求参数")
public class ShopCarCreateDto {

    @NotBlank(message = "请选择需要购买的商品")
    @ApiModelProperty(value = "商品ID")
    private String productId;

    @NotNull(message = "请选择需要购买的商品数量")
    @Max(value = 100, message = "购买数量超过最大限制")
    @Min(value = 1, message = "至少购买一件")
    @ApiModelProperty(value = "商品数量")
    private Integer num;

}
