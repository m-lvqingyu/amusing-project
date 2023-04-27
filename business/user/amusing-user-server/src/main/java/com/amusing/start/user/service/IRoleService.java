package com.amusing.start.user.service;

import com.amusing.start.exception.CustomException;
import com.amusing.start.user.constant.CacheKey;
import com.amusing.start.user.entity.dto.RoleAddDto;
import com.amusing.start.user.entity.dto.RoleMenuBindDto;
import com.amusing.start.user.entity.pojo.UserRoleInfo;
import com.amusing.start.user.entity.vo.RoleInfoVo;
import com.amusing.start.user.enums.UserStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RBucket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by 2023/04/11.
 *
 * @author lvqingyu
 */
public interface IRoleService {

    IPage<RoleInfoVo> list(String name, String code, Integer size, Integer page);

    Integer add(String userId, RoleAddDto dto) throws CustomException;

    Boolean menuBind(String userId, RoleMenuBindDto dto) throws CustomException;

    Integer getAdminRoleId();

    Boolean isAdmin(String userId) throws CustomException;

}
