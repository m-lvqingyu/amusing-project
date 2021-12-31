package com.amusing.start.client.input;

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
public class ShopPageInput {

    /**
     * 商铺名称
     */
    private String name;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 第几页
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;

}
