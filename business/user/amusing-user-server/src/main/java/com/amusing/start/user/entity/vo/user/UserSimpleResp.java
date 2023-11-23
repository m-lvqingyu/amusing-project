package com.amusing.start.user.entity.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "用户信息")
public class UserSimpleResp {

    @ApiModelProperty(value = "用户唯一ID")
    private String uId;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "来源(1:后台 2:APP)")
    private Integer sources;

    @ApiModelProperty(value = "状态(1:有效 2:冻结 3:无效)")
    private Integer status;

}
