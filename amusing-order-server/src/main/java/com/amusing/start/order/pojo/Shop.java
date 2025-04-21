package com.amusing.start.order.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @since 2023/9/20
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Shop {
    /**
     * 商铺ID
     */
    private String id;
    /**
     * 商铺名称
     */
    private String name;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 是否删除
     */
    private Integer isDel;
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