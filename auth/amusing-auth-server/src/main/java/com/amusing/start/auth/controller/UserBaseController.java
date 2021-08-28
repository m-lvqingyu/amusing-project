package com.amusing.start.auth.controller;

import com.amusing.start.auth.dto.LoginDTO;
import com.amusing.start.auth.from.LoginFrom;
import com.amusing.start.auth.service.UserBaseService;
import com.amusing.start.result.ApiResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
@RestController
public class UserBaseController {

    private final UserBaseService userBaseService;

    @Autowired
    public UserBaseController(UserBaseService userBaseService) {
        this.userBaseService = userBaseService;
    }

    @PostMapping("login")
    public ApiResult login(@Valid @RequestBody LoginFrom loginFrom) {
        LoginDTO loginDTO = build(loginFrom);
        ApiResult result = userBaseService.login(loginDTO);
        return result;
    }

    private LoginDTO build(LoginFrom loginFrom) {
        LoginDTO loginDTO = new LoginDTO();
        BeanUtils.copyProperties(loginFrom, loginDTO);
        return loginDTO;
    }
}
