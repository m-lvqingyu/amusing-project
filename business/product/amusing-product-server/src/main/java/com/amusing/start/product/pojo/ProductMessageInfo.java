package com.amusing.start.product.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductMessageInfo implements Serializable {

    private static final long serialVersionUID = -3957511839900701543L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 事务ID
     */
    private String txId;

    /**
     * 状态(0:待处理 1:已处理)
     */
    private Integer status;

    /**
     * 结果(1:成功 2:失败)
     */
    private Integer resultStatus;

    /**
     * 商铺ID
     */
    private String shopId;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 商品数量
     */
    private Integer productNum;

    /**
     * 商品单价ID
     */
    private String priceId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}