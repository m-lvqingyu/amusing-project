package com.amusing.start.user.service;

import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 手机号码分词加密Service
 * @since 2023/9/12
 */
public interface UserPhoneEncryptService {

    /**
     * 手机号码分词加密
     *
     * @param phone 手机号
     * @return 密文
     */
    Integer phoneKeywords(String userId, String phone);

    /**
     * 根据手机号模糊查询获取用户ID
     *
     * @param phone 手机号片段
     * @return 用户ID集合
     */
    List<String> getUserIdsByPhone(String phone);

    /**
     * 加密
     *
     * @param val 明文
     * @return 密文
     */
    String encrypt(String val);

    /**
     * 解密
     *
     * @param val 密文
     * @return 明文
     */
    String decrypt(String val);

    String phoneHandle(String phone);


}
