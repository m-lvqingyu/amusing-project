package com.amusing.start.auth.vo;

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
public class UserDetailVo {

    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 来源
     */
    private Integer sources;

    /**
     * imei
     */
    private String imei;
    
}
