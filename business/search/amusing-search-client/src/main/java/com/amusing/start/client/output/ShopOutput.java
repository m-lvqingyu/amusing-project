package com.amusing.start.client.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lv.qingyu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopOutput {

    /**
     * 主键
     */
    private Long id;

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
