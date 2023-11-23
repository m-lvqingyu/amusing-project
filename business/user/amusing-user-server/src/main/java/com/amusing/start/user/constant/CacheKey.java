package com.amusing.start.user.constant;

/**
 * @author Lv.QingYu
 * @description: 缓存KEY
 * @since 2023/03/06
 */
public class CacheKey {

    /**
     * 菜单根节点ID
     */
    public static final int MENU_ROOT_ID = 0;

    /**
     * 接口请求频次缓存Key
     *
     * @param ip  IP地址
     * @param uri 请求PATH
     * @return 接口请求频次缓存KEY
     */
    public static String reqLimitCacheKey(String ip, String uri) {
        return "request_limit:" + ip + ":" + uri;
    }

    /**
     * 用户角色关联关系缓存Key
     *
     * @param userId 用户ID
     * @return 缓存key
     */
    public static String userRoleCacheKey(String userId) {
        return "user_role:" + userId;
    }

    /**
     * 用户角色关联关系缓存时长
     */
    public static long USER_ROLE_CACHE_TIMEOUT = 30;

    /**
     * 角色菜单关联关系缓存key
     *
     * @param roleId 角色ID
     * @return 缓存key
     */
    public static String roleMenuCacheKey(Integer roleId) {
        return "role_menu:" + roleId;
    }

    /**
     * 角色菜单关联关系缓存时长
     */
    public static long ROLE_MENU_CACHE_TIMEOUT = 30;


}
