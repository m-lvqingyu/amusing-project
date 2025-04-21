package com.amusing.start.user.entity.request.menu;

import com.amusing.start.user.annotations.MenuStatusCheck;
import com.amusing.start.user.annotations.MenuTypeCheck;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Lv.QingYu
 * @since 2024/9/27
 */
@Schema(description = "菜单新增对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MenuAddRequest implements Serializable {

    private static final long serialVersionUID = -7662410026227555862L;

    @Schema(description = "菜单类型(1:菜单 2:按钮)")
    @MenuTypeCheck(message = "菜单类型错误")
    private Integer type;

    @Schema(description = "菜单名称")
    @Size(min = 2, max = 8, message = "菜单名称须在2-8个字符之间")
    private String name;

    @Schema(description = "菜单编码")
    @Size(min = 4, max = 12, message = "菜单编码须在4-12个字符之间")
    private String code;

    @Schema(description = "路径")
    @Size(max = 32, message = "菜单路径不能超过32个字符")
    private String path;

    @Schema(description = "优先级")
    @NotNull(message = "请选择菜单顺序")
    @Min(value = 1, message = "菜单顺序不能小于1")
    @Max(value = 10000, message = "菜单顺序不能超过10000")
    private Integer sort;

    @Schema(description = "父级菜单ID")
    @NotNull(message = "请选择父级菜单")
    private Integer parentId;

    @Schema(description = "状态(1:有效 5:无效)")
    @MenuStatusCheck(message = "状态无效")
    private Integer status;

}
