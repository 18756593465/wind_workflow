package com.bcx.wind.workflow;

import com.bcx.wind.workflow.core.OrderService;
import com.bcx.wind.workflow.core.TaskService;

/**
 * 运行时模块
 */
public class RuntimeServiceImpl implements RuntimeService {


    /**
     * 任务模块
     */
    private TaskService taskService;

    /**
     * 流程实例模块
     */
    private OrderService orderService;



    public RuntimeServiceImpl(TaskService taskService,OrderService orderService){
        this.taskService = taskService;
        this.orderService = orderService;
    }

    @Override
    public TaskService taskService() {
        return this.taskService;
    }


    @Override
    public OrderService orderService() {
        return this.orderService;
    }



}
