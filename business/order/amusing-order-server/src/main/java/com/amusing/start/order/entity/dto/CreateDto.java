package com.amusing.start.order.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Lv.QingYu
 * @since 2021/10/15
 */
@ApiModel(description = "创建订单请求参数")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateDto {

    @ApiModelProperty(value = "收件人")
    @NotBlank(message = "请选择收件人")
    @Pattern(regexp = "\\d{14,31}$", message = "收件人ID格式错误")
    private String consigneeId;

    @ApiModelProperty(value = "收件地址")
    @NotBlank(message = "请选择收件地址")
    @Pattern(regexp = "\\d{14,31}$", message = "收件地址ID格式错误")
    private String addressId;

}
