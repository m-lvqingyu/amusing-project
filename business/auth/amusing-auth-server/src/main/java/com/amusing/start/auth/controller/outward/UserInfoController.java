package com.amusing.start.auth.controller.outward;

import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.auth.exception.code.AuthCode;
import com.amusing.start.auth.pojo.SysUserBase;
import com.amusing.start.auth.service.IUserService;
import com.amusing.start.auth.vo.UserDetailVo;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author lv.qingyu
 */
@RestController
@RequestMapping("/auth/outward")
public class UserInfoController {

    private IUserService userService;

    @Autowired
    public UserInfoController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("v1/detail/{id}")
    public ApiResult<UserDetailVo> getDetail(@PathVariable("id") String userId) throws AuthException {
        Optional.ofNullable(userId).orElseThrow(() -> new AuthException(CommCode.PARAMETER_EXCEPTION));
        SysUserBase userBase = userService.queryByUserId(userId);
        Optional.ofNullable(userBase).orElseThrow(() -> new AuthException(AuthCode.USER_INFORMATION_NOT_EXIST));
        UserDetailVo vo = UserDetailVo.builder()
                .userId(userBase.getUserId())
                .userName(userBase.getUserName())
                .phone(userBase.getPhone())
                .sources(userBase.getSources())
                .imei(userBase.getImei())
                .build();
        return ApiResult.ok(vo);
    }

}
