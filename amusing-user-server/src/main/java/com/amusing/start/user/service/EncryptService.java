package com.amusing.start.user.service;

/**
 * @author Lv.QingYu
 * @since 2023/9/12
 */
public interface EncryptService {

    /**
     * 文本加密
     *
     * @param word 文本
     * @param len  分段长度
     * @return 加密字符
     */
    String keywords(String word, Integer len);

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

    /**
     * 隐藏手机号中间4位 替换字符为"*"
     *
     * @param phone 手机号（密文）
     * @return 处理结果
     */
    String phoneHandle(String phone);

}
