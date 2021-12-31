package com.amusing.start.search.controller.inward;

import com.amusing.start.client.api.IndexClient;
import com.amusing.start.client.input.IndexCreateInput;
import com.amusing.start.result.ApiResult;
import com.amusing.start.search.exception.SearchException;
import com.amusing.start.search.service.IndexService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lv.qingyu
 */
@Slf4j
@RestController
public class IndexInwardController implements IndexClient {

    private final IndexService indexService;

    @Autowired
    public IndexInwardController(IndexService indexService) {
        this.indexService = indexService;
    }

    @Override
    public ApiResult<Boolean> create(IndexCreateInput input) {

        try {
            boolean result = indexService.create(input.getIndex(), input.getProperties());
            return ApiResult.ok(result);
        } catch (SearchException e) {
            log.error("[index]-create err! param:{}, msg:{}", input, Throwables.getStackTraceAsString(e));
            return ApiResult.result(e.getResultCode());
        }

    }

    @Override
    public ApiResult<Boolean> exist(String index) {
        try {
            boolean result = indexService.exist(index);
            return ApiResult.ok(result);
        } catch (SearchException e) {
            log.error("[index]-checkout exist err! index:{}, msg:{}", index, Throwables.getStackTraceAsString(e));
            return ApiResult.result(e.getResultCode());
        }
    }

}
