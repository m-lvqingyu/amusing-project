package com.amusing.start.user.entity.request.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author Lv.QingYu
 * @since 2024/9/27
 */
@Schema(description = "菜单修改对象")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MenuEditRequest extends MenuAddRequest {

    private static final long serialVersionUID = -4715102072895634964L;

    @Schema(description = "菜单ID")
    @NotNull(message = "请选择需要修改的菜单")
    private Integer id;

}
