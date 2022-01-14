package com.amusing.start.auth.controller.outward;

import com.amusing.start.auth.dto.UserRegisterDto;
import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.auth.from.UserRegisterFrom;
import com.amusing.start.auth.service.IRegisterService;
import com.amusing.start.controller.BaseController;
import com.amusing.start.log.LogOutput;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("/auth/outward")
public class RegisterController extends BaseController {

    private final IRegisterService registerService;

    @Autowired
    public RegisterController(HttpServletRequest request, IRegisterService registerService) {
        super(request);
        this.registerService = registerService;
    }

    /**
     * 用户注册
     *
     * @param from 用户信息
     * @return 用户ID
     * @throws AuthException
     */
    @LogOutput
    @PostMapping("v1/user/register")
    public ApiResult<String> userRegister(@Valid @RequestBody UserRegisterFrom from) throws AuthException {
        UserRegisterDto registerDto = new UserRegisterDto();
        BeanUtils.copyProperties(from, registerDto);
        String userId = registerService.userRegister(registerDto);
        return ApiResult.ok(userId);
    }

    /**
     * 系统用户注册
     *
     * @return 用户ID
     * @throws AuthException
     */
    @PostMapping("v1/system/register")
    public ApiResult<String> systemRegister() throws AuthException {
        return ApiResult.ok();
    }

}
