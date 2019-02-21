package com.bcx.wind.workflow;

import com.bcx.wind.workflow.core.OrderService;
import com.bcx.wind.workflow.core.TaskService;

/**
 * 执行时模块
 *
 * @author zhanglei
 */
public interface RuntimeService {

    /**
     * 执行任务模块
     * @return  TaskService
     */
    TaskService taskService();


    /**
     * 流程实例模块
     * @return  OrderService
     */
    OrderService  orderService();




}
