package com.amusing.start.user.entity.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by 2023/04/12.
 *
 * @author lvqingyu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "用户信息")
public class UserListVo {

    @ApiModelProperty(value = "用户唯一ID", required = true)
    private String uId;

    @ApiModelProperty(value = "用户名", required = true)
    private String name;

    @ApiModelProperty(value = "手机号", required = true)
    private String phone;

    @ApiModelProperty(value = "来源(1:后台 2:APP)", required = true)
    private Integer sources;
    
    @ApiModelProperty(value = "状态(1:有效 2:冻结 3:无效)", required = true)
    private Integer status;

}
