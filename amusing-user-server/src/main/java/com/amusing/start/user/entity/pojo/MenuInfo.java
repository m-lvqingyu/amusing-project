package com.amusing.start.user.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MenuInfo implements Serializable {
    private static final long serialVersionUID = -2336312705444397507L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 菜单编码
     */
    private String code;
    /**
     * 类型(1:菜单 2:按钮)
     */
    private Integer type;
    /**
     * 路径
     */
    private String path;
    /**
     * 优先级
     */
    private Integer sort;
    /**
     * 父级菜单ID
     */
    private Integer parentId;
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