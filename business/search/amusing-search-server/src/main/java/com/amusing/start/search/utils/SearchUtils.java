package com.amusing.start.search.utils;

import com.amusing.start.search.constant.SearchConstant;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.Optional;

/**
 * @author lv.qingyu
 */
@Slf4j
public class SearchUtils {

    public static Integer pageSizeFilter(Integer pageSize) {
        return Optional.ofNullable(pageSize).filter(i -> i >= SearchConstant.MIN_PAGE_SIZE && i <= SearchConstant.MAX_PAGE_SIZE)
                .orElseGet(() -> SearchConstant.MAX_PAGE_SIZE);
    }

    public static Integer pageNumFilter(Integer pageNum) {
        return Optional.ofNullable(pageNum).filter(i -> i < SearchConstant.MIN_PAGE_NUM).orElseGet(() -> SearchConstant.MIN_PAGE_NUM);
    }

    public static String getRegexpValue(String v) {
        return ".*" + v + ".*";
    }

    public static void closeClient(RestHighLevelClient restHighLevelClient) {
        if (restHighLevelClient == null) {
            return;
        }
        try {
            restHighLevelClient.close();
        } catch (IOException e) {
            log.error("[search]-close client err! msg:{}", Throwables.getStackTraceAsString(e));
        }
    }

}
