package com.bcx.wind.workflow.access;

import java.util.List;

/**
 * 分页参数
 *
 * @author zhanglei
 */
public class FlowPage<T> implements Page<T> {

    private int pageSize = 10;

    private int pageNum = 1;

    private long total;

    private List<T>  list;

    @Override
    public int getPageSize() {
        return pageSize;
    }

    public FlowPage setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public int getPageNum() {
        return pageNum;
    }

    public FlowPage setPageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public List<T> result() {
        return list;
    }

    public FlowPage setTotal(long total) {
        this.total = total;
        return this;
    }


    public FlowPage result(List<T> list) {
        this.list = list;
        return this;
    }
}
