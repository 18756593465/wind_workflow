package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.*;
import com.bcx.wind.workflow.core.*;

/**
 * 工作流引擎创建器
 *
 * @author zhanglei
 */
public class WorkflowEngineBuilder {

    private WorkflowEngineBuilder(){}


    public static WorkflowEngine builder(Access access){
         WorkflowEngine engine = WorkflowEngineImpl.build(
                 repositoryService(access),
                 historyService(access),
                 runtimeService(access)
         );
         access.engine(engine);
         return engine;
    }


    /**
     * 静态资源模块创建
     *
     * @param access  数据模型
     * @return        静态资源模块
     */
    private static RepositoryService repositoryService(Access access){
        ProcessService processService = new ProcessServiceImpl(access);
        ProcessConfigurationService processConfigurationService = new ProcessConfigurationServiceImpl(access);
        return new RepositoryServiceImpl(processService,processConfigurationService);
    }


    /**
     * 历史履历模块
     *
     * @param access   数据模型
     * @return         历史履历模块
     */
    private static HistoryService historyService(Access access){
        ActiveHistoryService activeHistoryService = new ActiveHistoryServiceImpl(access);
        CompleteHistoryService completeHistoryService = new CompleteHistoryServiceImpl(access);
        OrderHistoryService orderHistoryService = new OrderHistoryServiceImpl(access);
        return new HistoryServiceImpl(activeHistoryService,completeHistoryService,orderHistoryService);
    }


    /**
     * 运行时模块
     *
     * @param access   数据模型
     * @return         运行时模块
     */
    private static RuntimeService runtimeService(Access access){
        OrderService orderService = new OrderServiceImpl(access);
        TaskService taskService = new TaskServiceImpl(access);
        return new RuntimeServiceImpl(taskService,orderService);
    }



}
