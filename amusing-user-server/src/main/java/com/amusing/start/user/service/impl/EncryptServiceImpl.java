package com.amusing.start.user.service.impl;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.amusing.start.user.service.EncryptService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Lv.QingYu
 * @since 2023/9/12
 */
@Service
public class EncryptServiceImpl implements EncryptService {

    @Value("amusing.phone.key.password")
    private String phoneKeyPassword;

    @Override
    public String keywords(String word, Integer len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            int end = i + len;
            String sub1 = word.substring(i, end);
            sb.append(encrypt(sub1));
            if (end == word.length()) {
                break;
            }
        }
        return sb.toString();
    }

    @Override
    public String encrypt(String val) {
        byte[] key = SecureUtil.generateKey(
                        SymmetricAlgorithm.DES.getValue(),
                        phoneKeyPassword.getBytes())
                .getEncoded();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.DES, key);
        return aes.encryptBase64(val);
    }

    @Override
    public String decrypt(String val) {
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue(),
                phoneKeyPassword.getBytes()).getEncoded();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.DES, key);
        return aes.decryptStr(val);
    }

    @Override
    public String phoneHandle(String phone) {
        if (StringUtils.isBlank(phone)) {
            return "";
        }
        phone = decrypt(phone);
        return PhoneUtil.hideBetween(phone).toString();
    }

}
