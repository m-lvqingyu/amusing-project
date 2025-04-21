package com.amusing.start.order.pojo;

import com.baomidou.mybatisplus.annotation.Version;
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
@AllArgsConstructor
@NoArgsConstructor
public class Price {
    /**
     * 价格ID
     */
    private String id;
    /**
     * 商品ID
     */
    private String productId;
    /**
     * 版本号
     */
    @Version
    private Integer version;
    /**
     * 商品价格
     */
    private Integer price;
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