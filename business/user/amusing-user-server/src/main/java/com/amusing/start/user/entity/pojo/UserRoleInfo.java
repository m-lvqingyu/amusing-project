package com.amusing.start.user.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleInfo {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 状态(1:有效 3:无效)
     */
    private Integer status;

    /**
     * 是否删除(1:未删除 2:已删除)
     */
    private Integer isDel;

    /**
     * 创建人ID
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新人ID
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Long updateTime;

}