package com.amusing.start.user.controller.api.user;

import com.amusing.start.controller.BaseController;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2024/12/6
 */
@Slf4j
@RequestMapping("/user/api")
@RestController
public class UserApiController extends BaseController {

    public UserApiController(HttpServletRequest request) {
        super(request);
    }

    @GetMapping("info")
    public ApiResult<?> info() {
//        String userId = getUserId();
        return ApiResult.ok("");
    }


}
