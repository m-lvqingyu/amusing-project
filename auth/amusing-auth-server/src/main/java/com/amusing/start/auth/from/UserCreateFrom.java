package com.amusing.start.auth.from;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Create By 2021/8/29
 *
 * @author lvqingyu
 */
@Data
public class UserCreateFrom {

    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotEmpty(message = "手机号不能为空")
    private String phone;

    @NotNull(message = "注册来源不能为空")
    private Integer sources;

    private String dingTalkId;

    private String companyWeChatId;

}
