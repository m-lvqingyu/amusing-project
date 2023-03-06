package com.amusing.start.user.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by 2022/10/2.
 *
 * @author lvqingyu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenVo {

    /**
     * token
     */
    private String token;

    /**
     * refreshToken
     */
    private String refreshToken;

}
