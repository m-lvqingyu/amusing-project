package com.amusing.start.user.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lv.qingyu
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
