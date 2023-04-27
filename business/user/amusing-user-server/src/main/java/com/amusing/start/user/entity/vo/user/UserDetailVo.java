package com.amusing.start.user.entity.vo.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by 2023/04/12.
 *
 * @author lvqingyu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailVo {

    /**
     * 用户唯一ID
     */
    private String uId;

    /**
     * 用户名
     */
    private String name;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 来源(1:后台 2:APP)
     */
    private Integer sources;

    /**
     * 状态(1:有效 2:冻结 3:无效)
     */
    private Integer status;

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

}
