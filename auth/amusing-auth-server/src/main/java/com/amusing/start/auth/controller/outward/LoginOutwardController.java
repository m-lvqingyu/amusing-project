package com.amusing.start.auth.controller.outward;

import com.amusing.start.auth.dto.LoginDTO;
import com.amusing.start.auth.from.LoginFrom;
import com.amusing.start.auth.service.LoginService;
import com.amusing.start.result.ApiResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Create By 2021/8/29
 *
 * @author lvqingyu
 */
@RestController
@RequestMapping("login")
public class LoginOutwardController {

    private final LoginService loginService;

    @Autowired
    public LoginOutwardController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ApiResult login(@Valid @RequestBody LoginFrom loginFrom) {
        LoginDTO loginDTO = new LoginDTO();
        BeanUtils.copyProperties(loginFrom, loginDTO);
        ApiResult result = loginService.login(loginDTO);
        return result;
    }
    
}
