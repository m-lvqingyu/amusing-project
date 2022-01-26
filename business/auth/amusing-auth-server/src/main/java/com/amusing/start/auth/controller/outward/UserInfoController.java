package com.amusing.start.auth.controller.outward;

import com.amusing.start.auth.convert.UserBaseConvert;
import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.auth.exception.code.AuthCode;
import com.amusing.start.auth.pojo.SysUserBase;
import com.amusing.start.auth.service.IUserService;
import com.amusing.start.auth.vo.UserDetailVo;
import com.amusing.start.code.CommCode;
import com.amusing.start.controller.BaseController;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author lv.qingyu
 */
@Slf4j
@RestController
@RequestMapping("/auth/outward")
public class UserInfoController extends BaseController {

    private IUserService userService;

    @Autowired
    public UserInfoController(HttpServletRequest request, IUserService userService) {
        super(request);
        this.userService = userService;
    }

    @GetMapping("v1/detail")
    public ApiResult<UserDetailVo> getDetail() throws AuthException {
        // 请求头中获取用户唯一ID
        String userId = getUserId();
        Optional.ofNullable(userId)
                .filter(StringUtils::isNotEmpty)
                .orElseThrow(() -> new AuthException(CommCode.PARAMETER_EXCEPTION));
        // 根据用户ID,获取用户信息
        SysUserBase userBase = userService.queryByUserId(userId);
        Optional.ofNullable(userBase)
                .orElseThrow(() -> new AuthException(AuthCode.USER_INFORMATION_NOT_EXIST));
        // 类型转换，返回用户VO信息
        UserDetailVo vo = UserBaseConvert.userConvertVo(userBase);
        return ApiResult.ok(vo);
    }


}
