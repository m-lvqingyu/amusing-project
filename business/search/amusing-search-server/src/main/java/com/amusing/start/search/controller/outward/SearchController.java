package com.amusing.start.search.controller.outward;

import com.amusing.start.code.ErrorCode;
import com.amusing.start.log.LogOutput;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lvqingyu on 2023/2/15.
 * Email: qingyu.lv@plusx.cn
 * Copyright(c) 2014 承影互联(科技)有限公司 版权所有
 */
@Slf4j
@RestController
@RequestMapping("search/outward")
public class SearchController {

    @LogOutput
    @PostMapping("v1/demo")
    public ApiResult<String> demo(@RequestBody String param) {
        return ApiResult.result(ErrorCode.SUCCESS);
    }
}
