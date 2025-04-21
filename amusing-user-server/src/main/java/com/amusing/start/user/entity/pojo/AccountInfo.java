package com.amusing.start.user.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AccountInfo implements Serializable {
    private static final long serialVersionUID = 4653108907425449398L;
    /**
     * 用户唯一ID
     */
    private String userId;
    /**
     * 主账户金额
     */
    private Integer mainAmount;
    /**
     * 冻结金额
     */
    private Integer frozenAmount;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Long updateTime;
}