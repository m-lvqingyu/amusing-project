package com.amusing.start.product.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lv.qingyu
 * @version 1.0
 * @date 2021/12/24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopCreateDto {

    /**
     * 商铺名称
     */
    private String shopName;

    /**
     * 等级
     */
    private Integer grade;

}
