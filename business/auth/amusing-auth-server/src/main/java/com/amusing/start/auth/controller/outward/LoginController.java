package com.amusing.start.auth.controller.outward;

import com.amusing.start.auth.dto.LoginDto;
import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.auth.from.LoginFrom;
import com.amusing.start.auth.service.ILoginService;
import com.amusing.start.auth.vo.TokenVo;
import com.amusing.start.code.CommCode;
import com.amusing.start.controller.BaseController;
import com.amusing.start.result.ApiResult;
import com.amusing.start.utils.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Create By 2021/8/29
 *
 * @author lvqingyu
 */
@RestController
@RequestMapping("/auth/outward")
public class LoginController extends BaseController {

    private final ILoginService loginService;

    @Autowired
    public LoginController(HttpServletRequest request, ILoginService loginService) {
        super(request);
        this.loginService = loginService;
    }

    /**
     * 用户登陆
     *
     * @param loginFrom 用户登陆信息
     * @return Token信息
     */
    @PostMapping("v1/login")
    public ApiResult<TokenVo> login(@Valid @RequestBody LoginFrom loginFrom) throws AuthException {
        LoginDto loginDTO = new LoginDto();
        BeanUtils.copyProperties(loginFrom, loginDTO);
        TokenVo tokenVo = loginService.login(loginDTO);
        return ApiResult.ok(tokenVo);
    }

    /**
     * token-刷新
     *
     * @param reToken 刷新Token
     * @return Token信息
     */
    @GetMapping("v1/refresh/{reToken}")
    public ApiResult<TokenVo> refresh(@PathVariable("reToken") String reToken) throws AuthException {
        if (StringUtils.isEmpty(reToken)) {
            return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
        }
        String imei = getImei();
        if (StringUtils.isEmpty(imei)) {
            return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
        }
        String userId = TokenUtils.getUserId(reToken);
        if (StringUtils.isEmpty(userId)) {
            return ApiResult.result(CommCode.UNAUTHORIZED);
        }
        TokenVo tokenVo = loginService.refresh(userId, reToken, imei);
        return ApiResult.ok(tokenVo);
    }
}
