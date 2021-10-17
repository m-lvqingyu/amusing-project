package com.amusing.start.product.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 商品信息
 */
@Data
public class ProductInfo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 商铺ID
     */
    private String shopId;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 初始商品单价
     */
    private BigDecimal initPrice;

    /**
     * 商品数量
     */
    private BigDecimal productStock;

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
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

}