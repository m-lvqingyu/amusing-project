package com.amusing.start.order.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 * @version 1.0
 * @date 2021/12/17 17:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDto {

    /**
     * 价格ID
     */
    private String productId;

    /**
     * 商品数量
     */
    private Integer productNum;

}
