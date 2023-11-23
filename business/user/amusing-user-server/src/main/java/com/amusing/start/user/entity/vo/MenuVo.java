package com.amusing.start.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "菜单信息")
public class MenuVo {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "类型：1:菜单 2:按钮")
    private Integer type;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "优先级")
    private Integer order;

    @ApiModelProperty(value = "父级ID")
    private Integer parentId;

    @ApiModelProperty(value = "级别")
    private Integer level;

    @ApiModelProperty(value = "子菜单")
    private List<MenuVo> children;

}
