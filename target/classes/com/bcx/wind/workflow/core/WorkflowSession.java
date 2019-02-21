package com.bcx.wind.workflow.core;

import com.bcx.wind.workflow.cache.DefaultCache;
import com.bcx.wind.workflow.core.handler.*;
import com.bcx.wind.workflow.core.pojo.*;

import java.util.List;

public interface WorkflowSession {


    /**
     * 创建工作流流
     *
     * @param variable   创建参数  processId or processName ,  businessId , user ， dataMap
     * @return      workflow
     */
    Workflow buildWorkflow(WorkflowVariable variable);

    /**
     * 创建工作流
     * @param user         当前用户
     * @param businessId   业务id集合
     * @return             创建结果
     */
    Workflow buildWorkflow(DefaultUser user, List<String> businessId);


    Workflow buildWorkflow(DefaultUser user,List<String> businessId,String suggest);


    /**
     * 创建工作流流
     *
     * @param variable   创建参数  processId or processName ,  businessId , user ， dataMap
     * @return      workflow
     */
    <T extends BuildHandler>Workflow buildWorkflow(WorkflowVariable variable,T  handler);

    /**
     * 创建工作流
     *
     * @param user         当前用户
     * @param businessId   业务id集合
     * @return             创建结果
     */
    <T extends BuildHandler>Workflow buildWorkflow( DefaultUser user, List<String> businessId,T handler);



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
     * @param orderId       流程实例号
     * @param user          当前用户
     * @param approveUsers  审批人
     * @return              提交结果
     */
    Workflow  submit(String orderId,DefaultUser user,List<ApproveUser>  approveUsers);


    /**
     * 提交工作流
     *
     * @param variable  提交参数   orderId  businessId  user dataMap approveUser  suggest  path
     * @return       workflow
     */
    <T extends SubmitHandler>Workflow  submit(WorkflowVariable variable , T handler);


    /**
     * 提交工作流
     *
     * @param orderId       流程实例号
     * @param user          当前用户
     * @param approveUsers  审批人
     * @return              提交结果
     */
    <T extends SubmitHandler>Workflow  submit(String orderId,DefaultUser user,List<ApproveUser>  approveUsers,T handler);

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
     * @param orderId  流程实例号
     * @param user     当前用户
     * @return         退回结果
     */
    Workflow  reject(String orderId,DefaultUser user);


    /**
     * 退回工作流
     *
     * @param variable   退回参数   orderId  businessId  dataMap  suggest   user
     * @return      workflow
     */
    <T extends RejectHandler>Workflow  reject(WorkflowVariable variable,T handler);

    /**
     * 退回工作流
     *
     * @param orderId  流程实例号
     * @param user     当前用户
     * @return         退回结果
     */
    <T extends RejectHandler>Workflow  reject(String orderId,DefaultUser user,T handler);


    /**
     * 撤回
     *
     * @param variable  businessId  user
     * @return    workflow
     */
    Workflow  withDraw(WorkflowVariable variable);


    /**
     * 撤回工作流
     *
     * @param businessId  业务id集合
     * @param user        当前用户
     * @return            撤回结果
     */
    Workflow  withDraw(List<String> businessId , DefaultUser user);


    /**
     * 撤回
     *
     * @param variable  businessId  user
     * @return workflow
     */
    <T extends WithdrawHandler>Workflow  withDraw(WorkflowVariable variable , T handler);


    /**
     * 撤回工作流
     *
     * @param businessId  业务id集合
     * @param user        当前用户
     * @return            撤回结果
     */
    <T extends WithdrawHandler>Workflow  withDraw(List<String> businessId , DefaultUser user, T handler);


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
     * @param orderId  流程实例号
     * @param user     当前用户
     * @return         完结结果
     */
    Workflow  complete(String orderId,DefaultUser user);


    /**
     * 完结
     *
     * @param variable  businessId  user
     * @return          workflow
     */
    <T extends CompleteHandler>Workflow  complete(WorkflowVariable variable,T handler);

    /**
     * 完结
     *
     * @param orderId  流程实例号
     * @param user     当前用户
     * @return         完结结果
     */
    <T extends CompleteHandler>Workflow  complete(String orderId,DefaultUser user,T handler);



    /**
     * 订阅
     *
     * @param variable  businessId  user
     * @return          workflow
     */
    Workflow  subScribe(WorkflowVariable variable);


    /**
     * 订阅
     *
     * @param orderId  流程实例号
     * @param user     当前用户
     * @return         完结结果
     */
    Workflow  subScribe(String orderId,DefaultUser user);


    /**
     * 订阅
     *
     * @param variable  businessId  user
     * @return          workflow
     */
    <T extends SubmitHandler>Workflow  subScribe(WorkflowVariable variable,T handler);

    /**
     * 订阅
     *
     * @param orderId  流程实例号
     * @param user     当前用户
     * @return         完结结果
     */
    <T extends SubmitHandler>Workflow  subScribe(String orderId,DefaultUser user,T handler);




}
