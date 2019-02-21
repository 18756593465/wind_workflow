package com.bcx.wind.workflow.core.handler;

import com.bcx.wind.workflow.core.pojo.Workflow;

/**
 * 退回操作拦截
 *
 * @author zhanglei
 */
public interface RejectHandler {


    /**
     * 退回操作前  执行
     *
     * @param workflow   工作流执行数据
     */
    void   beforeRejectHandler(Workflow workflow);


    /**
     * 提交操作后执行
     *
     * @param workflow  工作流执行数据
     */
    void   afterRejectHandler(Workflow workflow);
}
