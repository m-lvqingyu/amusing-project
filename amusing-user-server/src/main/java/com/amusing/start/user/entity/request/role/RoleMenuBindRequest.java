package com.amusing.start.user.entity.request.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @author Lv.QingYu
 * @description:
 * @since 2024/8/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuBindRequest {

    @NotNull(message = "角色ID不能为空")
    private Integer roleId;

    private List<Integer> menuIds;

}
