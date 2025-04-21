package com.amusing.start.user.entity.request.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2024/10/24
 */
@Schema(description = "员工账户对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdminAccountDetailRequest {

    @Schema(description = "员工ID")
    private String userId;

    @Schema(description = "主账户金额。单位：分")
    private Integer mainAmount;

    @Schema(description = "冻结金额。单位：分")
    private Integer frozenAmount;

}
