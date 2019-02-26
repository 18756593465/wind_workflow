package com.bcx.wind.workflow.executor;

import com.bcx.wind.workflow.core.handler.*;
import com.bcx.wind.workflow.core.pojo.Workflow;
import com.bcx.wind.workflow.core.pojo.WorkflowVariable;
import com.bcx.wind.workflow.executor.handler.ScribeTaskHandler;

/**
 * 工作流核心代理秘书
 *
 * @author zhanglei
 */
public interface Executor {

    /**
     * 创建工作流流
     *
     * @param variable   创建参数  processId or processName ,  businessId , user ， dataMap
     * @return      workflow
     */
    Workflow buildWorkflow(WorkflowVariable variable);


    /**
     * 创建工作流流
     *
     * @param variable   创建参数  processId or processName ,  businessId , user ， dataMap
     * @return      workflow
     */
    <T extends BuildHandler>Workflow buildWorkflow(WorkflowVariable variable,T  handler);



    /**
     * 提交工作流
     *
     * @param variable  提交参数   orderId  businessId  user dataMap approveUser  suggest  path
     * @return       workflow
     */
    Workflow  submit(WorkflowVariable variable);



    /**
     * 提交工作流
     *
     * @param variable  提交参数   orderId  businessId  user dataMap approveUser  suggest  path
     * @return       workflow
     */
    <T extends SubmitHandler>Workflow  submit(WorkflowVariable variable , T handler);


    /**
     * 退回工作流
     *
     * @param variable   退回参数   orderId  businessId  dataMap  suggest   user
     * @return      workflow
     */
    Workflow  reject(WorkflowVariable variable);



    /**
     * 退回工作流
     *
     * @param variable   退回参数   orderId  businessId  dataMap  suggest   user
     * @return      workflow
     */
    <T extends RejectHandler>Workflow  reject(WorkflowVariable variable,T handler);



    /**
     * 撤回
     *
     * @param variable  businessId  user
     * @return    workflow
     */
    Workflow  withDraw(WorkflowVariable variable);



    /**
     * 撤回
     *
     * @param variable  businessId  user
     * @return workflow
     */
    <T extends WithdrawHandler>Workflow  withDraw(WorkflowVariable variable , T handler);



    /**
     * 撤销
     *
     * @param variable   撤销参数
     * @return           workflow
     */
    Workflow  revoke(WorkflowVariable variable);


    /**
     * 撤销操作
     * @param variable  撤销参数
     * @param handler   前后置处理器
     * @param <T>       前后置实现
     * @return          workflow
     */
    <T extends RevokeHandler>Workflow revoke(WorkflowVariable variable,T handler);

    /**
     * 完结
     *
     * @param variable  businessId  user
     * @return          workflow
     */
    Workflow  complete(WorkflowVariable variable);



    /**
     * 完结
     *
     * @param variable  businessId  user
     * @return          workflow
     */
    <T extends CompleteHandler>Workflow  complete(WorkflowVariable variable,T handler);




}
