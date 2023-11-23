package com.amusing.start.user.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserInfo {

    /**
     * 主键
     */
    private String id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 密钥
     */
    private String secret;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 来源(1:后台 2:APP)
     */
    private Integer sources;

    /**
     * 状态(1:有效 2:冻结 3:无效)
     */
    private Integer status;

    /**
     * 是否删除(1:未删除 2:已删除)
     */
    private Integer isDel;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Long updateTime;

}