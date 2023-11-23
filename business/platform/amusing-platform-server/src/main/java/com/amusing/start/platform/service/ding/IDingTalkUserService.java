package com.amusing.start.platform.service.ding;

import com.amusing.start.client.output.DingTalkAccessToken;

/**
 * @author Lv.QingYu
 * @description: 钉钉用户信息API
 * @since 2023/10/3
 */
public interface IDingTalkUserService {

    /**
     * @return 钉钉AccessToken
     * @description: 获取企业内部AccessToken
     */
    DingTalkAccessToken getDingTalkAccessToken();

    /**
     * @param phone 手机号码
     * @return 钉钉用户ID
     * @description: 根据用户手机号获取钉钉用户ID
     */
    String getDingTalkUserId(String phone);

}
