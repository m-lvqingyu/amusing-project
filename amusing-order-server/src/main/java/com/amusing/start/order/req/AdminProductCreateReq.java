package com.amusing.start.order.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Lv.QingYu
 * @since 2021/12/24
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdminProductCreateReq {

    @NotBlank(message = "商铺ID不能为空")
    private String shopId;

    @NotBlank(message = "商品名称不能为空")
    private String name;

    @NotNull(message = "商品数量不能为空")
    private Integer stock;

    @NotNull(message = "商品单价不能为空")
    @Min(value = 0L, message = "商品单价不正确")
    private Integer price;
    
}
