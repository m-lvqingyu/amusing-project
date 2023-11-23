package com.amusing.start.user.mapper;

import com.amusing.start.user.entity.pojo.UserPhoneEncrypt;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 用户手机号密文与用户ID关联关系Mapper
 * @since 2023/9/12
 */
public interface UserPhoneEncryptMapper {

    Integer insert(UserPhoneEncrypt userPhoneEncrypt);

    Integer update(UserPhoneEncrypt userPhoneEncrypt);

    List<String> getByPhoneEncrypt(@Param("phoneEncrypt") String phoneEncrypt);

}
