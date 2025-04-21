package com.amusing.start.order.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Lv.QingYu
 * @description: 订单商铺关联关系表
 * @since 2021/10/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderShops implements Serializable {

    private static final long serialVersionUID = -5712182802241934263L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 商铺ID
     */
    private String shopsId;

    /**
     * 商铺名称
     */
    private String shopsName;

    /**
     * 排列顺序
     */
    private Integer sort;

}
