package com.amusing.start.order.from.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Administrator
 * @version 1.0
 * @date 2021/12/17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductFrom {

    /**
     * 价格ID
     */
    @NotEmpty(message = "商品已下架或不存在")
    private String productId;

    /**
     * 商品数量
     */
    @NotNull(message = "至少选择1件商品")
    private Integer productNum;

}
