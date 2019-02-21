package com.bcx.wind.workflow.core.handler;

import com.bcx.wind.workflow.core.pojo.Workflow;

/**
 * 撤回拦截
 *
 * @author zhanglei
 */
public interface WithdrawHandler {


    /**
     * 撤回操作前执行
     *
     * @param workflow  工作流执行数据
     */
    void beforeWithdrawHandler(Workflow workflow);


    /**
     * 撤回操作后执行
     * @param workflow  工作流执行数据
     */
    void  afterWithdrawHandler(Workflow workflow);
}
