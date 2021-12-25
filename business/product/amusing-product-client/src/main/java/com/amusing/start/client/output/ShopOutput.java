package com.amusing.start.client.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopOutput {

    /**
     * 商铺ID
     */
    private String shopId;

    /**
     * 商铺名称
     */
    private String shopName;

    /**
     * 等级
     */
    private Integer grade;

}
