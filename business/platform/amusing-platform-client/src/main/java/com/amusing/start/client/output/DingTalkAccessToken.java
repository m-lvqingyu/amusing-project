package com.amusing.start.client.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @description: 钉钉企业内部应用的accessToken
 * @since 2023/10/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DingTalkAccessToken {

    /**
     * 生成的accessToken
     */
    private String accessToken;
    
}
