package com.amusing.start.platform.service.oauth.ali;

/**
 * @author Lv.QingYu
 * @description: PC 网页内获取用户信息
 * @link <a href="https://opendocs.alipay.com/open/284/web/">...</a>
 * @since 2023/11/10
 */
public interface AliPcOauthService {

    /**
     * @param appId 支付宝AppId
     * @param authCode 授权码
     * @return userId - 支付宝用户ID
     * @description: 使用 auth_code 换取 access_token 及用户的 user_id
     */
    String getUserId(String appId, String authCode);

}
