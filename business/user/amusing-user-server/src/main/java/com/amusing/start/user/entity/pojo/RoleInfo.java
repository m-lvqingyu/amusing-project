package com.amusing.start.user.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by 2023/2/9.
 *
 * @author lvqingyu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleInfo {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 名称码
     */
    private String nameCode;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 状态(1:有效 2:冻结 3:无效)
     */
    private Integer status;

    /**
     * 是否删除(1:未删除 2:已删除)
     */
    private Integer isDel;

    /**
     * 管理员: 1:是 0:否
     */
    private Integer isAdmin;

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
