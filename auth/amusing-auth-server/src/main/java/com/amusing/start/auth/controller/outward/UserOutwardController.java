package com.amusing.start.auth.controller.outward;

import com.amusing.start.auth.dto.UserCreateDto;
import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.auth.from.UserCreateFrom;
import com.amusing.start.auth.service.UserService;
import com.amusing.start.code.CommCode;
import com.amusing.start.controller.BaseController;
import com.amusing.start.result.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
@RestController
@RequestMapping("/user")
public class UserOutwardController extends BaseController {

    private final UserService userBaseService;

    @Autowired
    public UserOutwardController(HttpServletRequest request, UserService userBaseService) {
        super(request);
        this.userBaseService = userBaseService;
    }

    /**
     * 创建用户
     *
     * @param from
     * @return
     * @throws AuthException
     */
    @PostMapping
    public ApiResult create(@Valid @RequestBody UserCreateFrom from) throws AuthException {
        String executorUserId = getUserId();
        if (StringUtils.isEmpty(executorUserId)) {
            throw new AuthException(CommCode.UNAUTHORIZED);
        }

        UserCreateDto createDTO = new UserCreateDto();
        BeanUtils.copyProperties(from, createDTO);
        ApiResult result = userBaseService.create(executorUserId, createDTO);
        return result;
    }

}
