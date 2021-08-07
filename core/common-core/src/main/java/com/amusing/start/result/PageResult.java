package com.amusing.start.result;

import lombok.Data;

import java.util.List;

@Data
public class PageResult {

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


    private List list;

    public PageResult() {
    }

    public PageResult(long page, long total, long size, long current, List list) {
        this.page = page;
        this.total = total;
        this.size = size;
        this.current = current;
        this.list = list;
    }

}
