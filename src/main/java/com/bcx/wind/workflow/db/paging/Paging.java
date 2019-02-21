package com.bcx.wind.workflow.db.paging;

import com.bcx.wind.workflow.access.FlowPage;

/**
 * 分页组件
 *
 * @author zhanglei
 */
public interface Paging {


    /**
     * 创建分页查询语句
     *
     * @param sql   原始查询语句
     * @param page  分页参数
     * @return      分页语句
     */
    StringBuilder  buildPageSql(StringBuilder sql, FlowPage page);
}
