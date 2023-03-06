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
public class MenuInfo {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 名称码
     */
    private String nameCode;

    /**
     * 类型：1:菜单 2:按钮
     */
    private Integer type;

    /**
     * 组件
     */
    private String component;

    /**
     * 路径
     */
    private String path;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 按钮：add-添加,delete-删除,edit-编辑,query-查询
     */
    private String icon;

    /**
     * 描述
     */
    private String description;

    /**
     * 父级ID
     */
    private Integer parentId;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 状态(1:有效 2:冻结 3:无效)
     */
    private Integer status;

    /**
     * 是否删除(1:未删除 2:已删除)
     */
    private Integer isDel;

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
