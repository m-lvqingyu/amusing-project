package com.amusing.start.user.entity.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Lv.QingYu
 * @since 2024/10/23
 */
@Schema(description = "员工列表对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdminUserPageRequest {

    @Schema(description = "员工姓名", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "张三")
    private String name;

    @Schema(description = "手机号码", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "18888888888")
    private String phone;

    @Schema(description = "来源(1:后台 2:APP)", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
    private Integer sources;

    @Schema(description = "状态(1:有效 2:冻结 3:无效)", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "页码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "请选择页码")
    @Min(value = 1, message = "页码不能小于1")
    @Max(value = 10000, message = "页码不能超过10000")
    private Integer page;

    @Schema(description = "行数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "请选择行数")
    @Min(value = 1, message = "行数不能小于1")
    @Max(value = 50, message = "行数不能超过10000")
    private Integer size;

}
