package com.amusing.start.user.controller.outward;

import com.amusing.start.code.ErrorCode;
import com.amusing.start.controller.BaseController;
import com.amusing.start.exception.CustomException;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.annotation.RequestLimit;
import com.amusing.start.user.entity.dto.LoginDto;
import com.amusing.start.user.entity.dto.RegisterDto;
import com.amusing.start.user.entity.vo.TokenVo;
import com.amusing.start.user.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@RestController
@RequestMapping("user/outward")
public class LoginController extends BaseController {

    private final IUserService userService;

    @Autowired
    LoginController(HttpServletRequest request, IUserService userService) {
        super(request);
        this.userService = userService;
    }

    @GetMapping("v1/register/check")
    @RequestLimit(count = 2)
    public ApiResult<Boolean> check(@RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "phone", required = false) String phone) throws CustomException {
        if (StringUtils.isBlank(name) && StringUtils.isBlank(phone)) {
            throw new CustomException(ErrorCode.PARAMETER_ERR);
        }
        return ApiResult.ok(userService.registerCheck(name, phone));
    }

    @PostMapping("v1/register")
    public ApiResult<Boolean> register(@Valid @RequestBody RegisterDto dto) throws CustomException {
        return ApiResult.ok(userService.register(dto));
    }

    @PostMapping("v1/login")
    public ApiResult<TokenVo> login(@Valid @RequestBody LoginDto dto) throws CustomException {
        return ApiResult.ok(userService.login(dto));
    }

    @GetMapping("v1/refresh/{token}")
    public ApiResult<TokenVo> refresh(@PathVariable("token") String token) throws CustomException {
        return ApiResult.ok(userService.refresh(token));
    }

}
