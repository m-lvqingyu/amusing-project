package com.amusing.start.auth.controller.outward;

import com.amusing.start.auth.dto.UserCreateDTO;
import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.auth.from.UserCreateFrom;
import com.amusing.start.auth.service.UserService;
import com.amusing.start.result.ApiResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
@RestController
@RequestMapping("/user")
public class UserOutwardController {

    private final UserService userBaseService;

    @Autowired
    public UserOutwardController(UserService userBaseService) {
        this.userBaseService = userBaseService;
    }

    @PostMapping
    public ApiResult create(@Valid @RequestBody UserCreateFrom from) throws AuthException {
        UserCreateDTO createDTO = new UserCreateDTO();
        BeanUtils.copyProperties(from, createDTO);
        ApiResult result = userBaseService.create(createDTO);
        return result;
    }
    
}
