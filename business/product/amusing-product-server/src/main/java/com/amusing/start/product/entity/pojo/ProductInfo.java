package com.amusing.start.product.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @description: 商品信息表
 * @since 2023/9/20
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {

    /**
     * 商铺ID
     */
    private String shopId;

    /**
     * 商品ID
     */
    private String id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品数量
     */
    private Integer stock;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 是否删除
     */
    private Integer isDel;

    /**
     * 描述
     */
    private String describe;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Long updateTime;

}