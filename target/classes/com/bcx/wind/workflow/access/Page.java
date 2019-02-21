package com.bcx.wind.workflow.access;

import java.util.List;

/**
 * 分页数据类型
 * @param <T>
 */
public interface Page<T> {

    int getPageSize();

    int getPageNum();

    long getTotal();

    List<T>  result();
}
