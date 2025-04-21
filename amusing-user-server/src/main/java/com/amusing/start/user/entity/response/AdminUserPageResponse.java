package com.amusing.start.user.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @since 2024/10/23
 */
@Schema(description = "员工列表响应对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AdminUserPageResponse {

    @Schema(description = "员工ID")
    private String id;

    @Schema(description = "员工姓名")
    private String name;

    @Schema(description = "员工手机号码")
    private String phone;

    @Schema(description = "来源(1:后台 2:APP)")
    private Integer sources;

    @Schema(description = "登录版本号")
    private Long version;

    @Schema(description = "状态(1:有效 2:冻结 3:无效)")
    private Integer status;

    @Schema(description = "创建时间")
    private Long createTime;

}
