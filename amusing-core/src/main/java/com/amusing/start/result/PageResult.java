package com.amusing.start.result;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class PageResult<T> {
    /**
     * 总页数
     */
    private long page;
    /**
     * 总条数
     */
    private long total;
    /**
     * 每页条数
     */
    private long size;
    /**
     * 当前页
     */
    private long current;

    private List<T> list;

    public PageResult() {
    }

    public PageResult(long page, long total, long size, long current, List<T> list) {
        this.page = page;
        this.total = total;
        this.size = size;
        this.current = current;
        this.list = list;
    }

}
