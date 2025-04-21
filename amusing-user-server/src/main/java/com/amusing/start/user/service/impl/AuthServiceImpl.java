package com.amusing.start.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.exception.CustomException;
import com.amusing.start.user.business.RoleMenuBusiness;
import com.amusing.start.user.entity.pojo.UserInfo;
import com.amusing.start.user.entity.response.MenuResponse;
import com.amusing.start.user.enums.UserErrorCode;
import com.amusing.start.user.enums.UserStatus;
import com.amusing.start.user.service.AuthService;
import com.amusing.start.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2024/8/27
 */
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final RoleMenuBusiness roleMenuBusiness;

    private final AntPathMatcher antPathMatcher;

    private final UserService userService;

    @Override
    public Boolean auth(String userId, Long version, String uri) {
        UserInfo userInfo = userService.getById(userId, UserStatus.VALID.getKey());
        if (!userInfo.getVersion().equals(version)) {
            throw new CustomException(UserErrorCode.TOKEN_INVALID);
        }
        List<MenuResponse> menuTree = roleMenuBusiness.getMenuTree(userId);
        if (CollectionUtil.isEmpty(menuTree)) {
            return Boolean.FALSE;
        }
        return doMatch(uri, menuTree);
    }

    public Boolean doMatch(String uri, List<MenuResponse> responseList) {
        if (!CollectionUtils.isEmpty(responseList)) {
            for (MenuResponse menuResponse : responseList) {
                String path = menuResponse.getPath();
                if (antPathMatcher.match(path, uri)) {
                    return Boolean.TRUE;
                }
                List<MenuResponse> children = menuResponse.getChildren();
                if (doMatch(uri, children)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

}
