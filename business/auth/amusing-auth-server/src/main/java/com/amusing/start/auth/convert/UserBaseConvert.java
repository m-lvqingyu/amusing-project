package com.amusing.start.auth.convert;

import com.amusing.start.auth.constant.AuthConstant;
import com.amusing.start.auth.pojo.SysUserBase;
import com.amusing.start.auth.vo.UserDetailVo;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lv.qingyu
 */
public class UserBaseConvert {

    public static UserDetailVo userConvertVo(SysUserBase userBase) {
        return UserDetailVo.builder()
                .userId(userBase.getUserId())
                .userName(userBase.getUserName())
                .phone(phoneConvert(userBase.getPhone()))
                .sources(userBase.getSources())
                .build();
    }

    public static String phoneConvert(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return "";
        }
        return new StringBuilder().append(phone, AuthConstant.ZERO, AuthConstant.THREE)
                .append(AuthConstant.FOUR_ASTERISK)
                .append(phone.substring(AuthConstant.SEVEN)).toString();

    }

}
