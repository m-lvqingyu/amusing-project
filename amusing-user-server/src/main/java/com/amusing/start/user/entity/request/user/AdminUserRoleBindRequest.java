package com.amusing.start.user.entity.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2024/10/24
 */
@Schema(description = "员工角色绑定对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdminUserRoleBindRequest {

    @Schema(description = "员工ID", example = "8273891831923")
    @NotBlank(message = "员工ID不能为空")
    private String userId;

    @Schema(description = "角色集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<Integer> roleIds;

}
