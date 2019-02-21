package com.bcx.wind.workflow.core;

import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.ActiveHistory;

import java.util.List;

/**
 * 执行历史模块
 *
 * @author zhanglei
 */
public interface ActiveHistoryService {

    /**
     * 过滤查询历史
     *
     * @param filter 过滤条件
     * @return       审批历史集合
     */
    List<ActiveHistory>  queryList(QueryFilter filter);


    /**
     * 过滤分页查询
     * @param filter  过滤条件
     * @param page    分页条件
     * @return        查询结果
     */
    List<ActiveHistory>  queryList(QueryFilter filter, FlowPage<ActiveHistory> page);


    /**
     * 通过主键查询
     * @param id   主键
     * @return     activeHistory
     */
    ActiveHistory   queryById(String id);


    /**
     * 新增
     * @param activeHistory   审批历史
     * @return                添加结果
     */
    int insert(ActiveHistory activeHistory);


    /**
     * 更新审批历史
     * @param activeHistory  审批历史
     * @return               更新结果
     */
    int  update(ActiveHistory activeHistory);


    /**
     * 通过ID删除历史
     *
     * @param id  ID
     * @return    删除结果
     */
    int   deleteById(String id);


    /**
     * 批量删除
     * @param ids  id数组
     * @return     删除结果
     */
    int   deleteByIds(List<String> ids);

}
