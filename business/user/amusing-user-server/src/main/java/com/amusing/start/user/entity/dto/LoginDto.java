package com.amusing.start.user.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * Created by lvqingyu on 2022/10/3.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    
    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    private String password;

}
