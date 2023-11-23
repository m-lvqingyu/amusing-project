package com.amusing.start.user.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AccountInfo {

    /**
     * 用户唯一ID
     */
    private String userId;

    /**
     * 主账户金额
     */
    private Integer mainAmount;

    /**
     * 副账户金额
     */
    private Integer giveAmount;

    /**
     * 冻结金额
     */
    private Integer frozenAmount;

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