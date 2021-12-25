package com.amusing.start.user.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 用户账户表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountInfo {

    private Long id;

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