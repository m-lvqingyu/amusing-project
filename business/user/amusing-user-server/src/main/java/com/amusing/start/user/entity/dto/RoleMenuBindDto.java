package com.amusing.start.user.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by 2023/04/11.
 *
 * @author lvqingyu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuBindDto {

    @NotNull(message = "角色ID不能为空")
    private Integer roleId;

    private List<Integer> menuIds;

}
