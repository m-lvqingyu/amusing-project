package com.amusing.start.product.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 商品价格信息
 */
@Data
public class ProductPriceInfo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 价格ID
     */
    private String priceId;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 活动ID
     */
    private String activityId;

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