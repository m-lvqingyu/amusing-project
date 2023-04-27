package com.amusing.start.user.constant;

/**
 * Created by 2022/10/2.
 *
 * @author lvqingyu
 */
public class CacheKey {

    public static final int MENU_ROOT_ID = 0;

    public static final int SEVEN = 7;

    public static final int ONE = 1;

    public static String requestLimitKey(String ip, String uri) {
        return "request_limit:" + ip + ":" + uri;
    }

    public static String userRoleKey(String userId) {
        return "user_role:" + userId;
    }

    public static String adminRoleKey() {
        return "admin_role_id";
    }

}
