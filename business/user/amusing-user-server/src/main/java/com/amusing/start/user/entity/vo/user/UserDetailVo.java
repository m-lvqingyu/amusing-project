package com.amusing.start.user.entity.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "用户详情")
public class UserDetailVo {

    @ApiModelProperty(value = "用户唯一ID")
    private String uId;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "来源(1:后台 2:APP)")
    private Integer sources;

    @ApiModelProperty(value = "状态(1:有效 2:冻结 3:无效)")
    private Integer status;

//    @ApiModelProperty(value = "主账户金额")
//    private Integer mainAmount;
//
//    @ApiModelProperty(value = "副账户金额")
//    private Integer giveAmount;
//
//    @ApiModelProperty(value = "冻结金额")
//    private Integer frozenAmount;
//
//    @ApiModelProperty(value = "会员等级")
//    private Integer vipLevel;

}
