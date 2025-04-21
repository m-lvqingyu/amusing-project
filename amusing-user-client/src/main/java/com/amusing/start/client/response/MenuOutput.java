package com.amusing.start.client.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuOutput {

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
     * 子菜单
     */
    private List<MenuOutput> children;

}
