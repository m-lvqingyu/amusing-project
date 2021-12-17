package com.amusing.start.client.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 * @version 1.0
 * @date 2021/12/17 14:22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockDeductionInput {

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 商品数量
     */
    private Integer productNum;

}
