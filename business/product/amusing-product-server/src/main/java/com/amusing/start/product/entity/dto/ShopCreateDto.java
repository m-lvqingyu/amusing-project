package com.amusing.start.product.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Lv.QingYu
 * @description: 商铺新增请求参数
 * @since 2021/12/24
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "商铺新增请求参数")
public class ShopCreateDto {

    @ApiModelProperty(value = "商铺名称")
    @NotBlank(message = "商铺名称不能为空")
    @Length(min = 1, max = 31, message = "商铺名称不合规")
    private String shopName;

    @ApiModelProperty(value = "等级")
    @NotNull(message = "等级不能为空")
    private Integer grade;

}
