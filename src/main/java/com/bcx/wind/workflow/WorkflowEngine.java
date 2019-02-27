package com.bcx.wind.workflow;

import com.bcx.wind.workflow.core.WorkflowService;
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
     * 主要功能：  创建流程 ，提交流程，退回流程，撤销流程，撤回流程，完结流程。
     *
     * @return 工作流核心接口
     */
    WorkflowSession openWorkflowSession(String processName);


    /**
     * 工作流辅助功能接口
     *
     * @return 辅助接口  主要实现转办关闭  暂停恢复流程  流程监控查询等
     */
    WorkflowService openWorkflowService();


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
