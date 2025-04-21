package com.amusing.start.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @since 2024/8/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserLoginResponse {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 版本号
     */
    private Long version;
}
