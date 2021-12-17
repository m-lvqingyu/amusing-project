package com.amusing.start.order.from.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lv.qingyu
 * @version 1.0
 * @date 2021/12/17 17:37
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderShopFrom {

    @NotEmpty(message = "商铺信息不存在")
    private String shopsId;

    @NotNull(message = "至少选择1件商品")
    List<OrderProductFrom> productFromList;

}
