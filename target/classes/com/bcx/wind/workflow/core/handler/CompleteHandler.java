package com.bcx.wind.workflow.core.handler;

import com.bcx.wind.workflow.core.pojo.Workflow;

/**
 * 完结拦截
 *
 * @author zhanglei
 */
public interface CompleteHandler {


    /**
     * 完结操作前 执行
     * @param workflow  工作流执行数据
     */
    void  beforeCompleteHandler(Workflow workflow);


    /**
     * 完结操作后执行
     *
     * @param workflow  工作流执行数据
     */
    void  afterCompleteHandler(Workflow workflow);
}
