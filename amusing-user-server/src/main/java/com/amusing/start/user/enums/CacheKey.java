package com.amusing.start.user.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @since 2024/9/27
 */
@Getter
public enum CacheKey {

    MENU_CACHE("menu:all", 5);

    private final String key;

    private final Integer defTimeToLive;

    CacheKey(String key, Integer defTimeToLive) {
        this.key = key;
        this.defTimeToLive = defTimeToLive;
    }

}
