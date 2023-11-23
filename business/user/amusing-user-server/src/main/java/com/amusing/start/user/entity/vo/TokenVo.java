package com.amusing.start.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Token信息")
public class TokenVo {

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "refreshToken")
    private String refreshToken;

}
