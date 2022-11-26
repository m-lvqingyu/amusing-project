package com.amusing.start.order.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 创建订单From
 * @date 2021/10/15 16:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class CreateDto {

    @ApiModelProperty(value = "收件人")
    @NotBlank(message = "请选择收件人")
    private String consigneeId;

    @ApiModelProperty(value = "收件地址")
    @NotBlank(message = "请选择收件地址")
    private String addressId;

}
