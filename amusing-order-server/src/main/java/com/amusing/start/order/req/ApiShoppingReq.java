package com.amusing.start.order.req;

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
 * @since 2023/9/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ApiShoppingReq {

    @NotBlank(message = "请选择需要购买的商品")
    private String productId;

    @NotNull(message = "请选择需要购买的商品数量")
    @Max(value = 100, message = "购买数量超过最大限制")
    @Min(value = 1, message = "至少购买一件")
    private Integer num;

}
