package com.bcx.wind.workflow;

import com.bcx.wind.workflow.core.WorkflowSession;

/**
 * 工作流核心接口
 *
 * date: 2019-02-11
 *
 * version 1.1
 *
 * @author zhanglei
 */
public interface WorkflowEngine {


    /**
     *工作流静态资源模块
     *
     * @return   RepositoryService
     */
    RepositoryService repositoryService();


    /**
     * 历史履历模块
     *
     * @return  HistoryService
     */
    HistoryService historyService();


    /**
     * 执行时模块
     *
     * @return  RuntimeService
     */
    RuntimeService runtimeService();


    /**
     *
     * @return 工作流核心接口
     */
    WorkflowSession openWorkflowSession(String processName);


    /**
     * bean容器
     *
     * @return  context
     */
    Context  getContext();


    /**
     * 设置容器
     * @param context 容器
     */
    void context(Context context);



}
