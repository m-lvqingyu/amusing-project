package com.amusing.start.product.from.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author lv.qingyu
 * @version 1.0
 * @date 2021/12/24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateFrom {

    /**
     * 商铺ID
     */
    @NotEmpty(message = "请选择商铺")
    private String shopId;

    /**
     * 商品名称
     */
    @NotEmpty(message = "请填写商品名称")
    private String name;

    /**
     * 商品数量
     */
    @NotNull(message = "请填写商品数量")
    private BigDecimal stock;

    /**
     * 商品单价
     */
    @NotNull(message = "请填写商品单价")
    private BigDecimal price;

    /**
     * 描述
     */
    private String describe;

}
