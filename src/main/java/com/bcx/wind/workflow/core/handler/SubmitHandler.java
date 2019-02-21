package com.bcx.wind.workflow.core.handler;


import com.bcx.wind.workflow.core.pojo.Workflow;

/**
 * 提交工作流拦截操作
 *
 * @author zhanglei
 */
public interface SubmitHandler {


    /**
     * 提交操作 前置调用   操作前调用
     *
     * @param workflow   工作流执行数据
     */
    void  beforeSubmitHandler(Workflow workflow);


    /**
     * 提交操作 后置调用   结束前调用
     *
     * @param workflow   工作流执行数据
     */
    void  afterSubmitHandler(Workflow workflow);
}
