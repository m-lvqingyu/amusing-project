package com.amusing.start.user.utils;

import com.amusing.start.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/9/19
 */
@Slf4j
public class PageUtil<T> {

    public static <T> Page<T> transform(Page<?> page, Class<T> classType) {
        Page<T> pg = new Page<>();
        try {
            pg.setRecords(copyList(page.getRecords(), classType));
        } catch (Exception e) {
            log.error("PageUtil transform error", e);
        }
        pg.setTotal(page.getTotal());
        pg.setSize(page.getSize());
        pg.setCurrent(page.getCurrent());
        pg.setOrders(page.orders());
        pg.setOptimizeCountSql(pg.optimizeCountSql());
        pg.setSearchCount(pg.searchCount());
        return pg;
    }

    public static <T> List<T> copyList(List<?> source, Class<T> clazz) {
        if (source == null || source.size() == 0) {
            return Collections.emptyList();
        }
        List<T> res = new ArrayList<>(source.size());
        for (Object o : source) {
            try {
                T t = clazz.newInstance();
                res.add(t);
                BeanUtils.copyProperties(o, t);
            } catch (Exception e) {
                log.error("PageUtil copyList error", e);
            }
        }
        return res;
    }

    public static <T> PageResult<T> buildDef(Page<?> page) {
        return new PageResult<>(page.getPages(), page.getTotal(), page.getSize(), page.getCurrent(), new ArrayList<>());
    }
}
