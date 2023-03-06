package com.amusing.start.user.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by 2023/2/9.
 *
 * @author lvqingyu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {

    /**
     * 用户唯一ID
     */
    private String userId;

    /**
     * 主账户金额
     */
    private BigDecimal mainAmount;

    /**
     * 副账户金额
     */
    private BigDecimal giveAmount;

    /**
     * 冻结金额
     */
    private BigDecimal frozenAmount;

    /**
     * 会员等级
     */
    private Integer vipLevel;

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