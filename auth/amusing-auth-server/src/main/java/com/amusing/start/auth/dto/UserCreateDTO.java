package com.amusing.start.auth.dto;

import lombok.Data;

import java.util.Date;

/**
 * Create By 2021/8/29
 *
 * @author lvqingyu
 */
@Data
public class UserCreateDTO {

    /**
     * 用户唯一ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

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
     * 来源
     */
    private Integer sources;

    /**
     * 钉钉用户ID
     */
    private String dingTalkId;

    /**
     * 企业微信用户ID
     */
    private String companyWeChatId;

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
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

}
