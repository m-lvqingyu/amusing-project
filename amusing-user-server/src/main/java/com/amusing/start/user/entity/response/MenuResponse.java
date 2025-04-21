package com.amusing.start.user.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2024/8/6
 */
@Schema(description = "菜单响应对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MenuResponse {

    @Schema(description = "菜单ID")
    private Integer id;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "菜单编码")
    private String code;

    @Schema(description = "菜单类型(1:菜单 2:按钮)")
    private Integer type;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "优先级")
    private Integer sort;

    @Schema(description = "父级菜单ID")
    private Integer parentId;

    @Schema(description = "子菜单集合")
    private List<MenuResponse> children;

}
