package com.amusing.start.user.entity.response.api.user;

import com.amusing.start.user.entity.response.api.role.RoleDetailResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2024/12/6
 */
@Schema(description = "员工响应对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserDetailResponse {

    @Schema(description = "员工ID")
    private String id;

    @Schema(description = "员工用户名")
    private String name;

    @Schema(description = "员工手机号")
    private String phone;

    @Schema(description = "注册来源(1:后台 2:APP)")
    private Integer sources;

    @Schema(description = "主账户金额")
    private Integer mainAmount;

    @Schema(description = "冻结金额")
    private Integer frozenAmount;

    @Schema(description = "角色集合")
    private List<RoleDetailResponse> roleList;

}
