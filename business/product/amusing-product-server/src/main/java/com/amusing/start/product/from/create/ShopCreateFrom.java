package com.amusing.start.product.from.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author lv.qingyu
 * @version 1.0
 * @date 2021/12/24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopCreateFrom {

    /**
     * 商铺名称
     */
    @NotEmpty(message = "请输入商铺名称")
    private String name;

    /**
     * 等级
     */
    @NotNull(message = "请选择商铺等级")
    private Integer grade;

}
