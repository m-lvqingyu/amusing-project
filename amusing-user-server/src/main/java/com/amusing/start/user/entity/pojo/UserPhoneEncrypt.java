package com.amusing.start.user.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @description: 用户手机号码密文映射表
 * @since 2023/9/12
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoneEncrypt {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户唯一ID
     */
    private String userId;

    /**
     * 手机号码分词密文
     */
    private String phoneKey;
    
}
