package com.bcx.wind.workflow.core.handler;

import com.bcx.wind.workflow.core.pojo.Workflow;

/**
 * 创建工作流拦截
 *
 * @author zhanglei
 */
public interface BuildHandler {


    /**
     * 创建工作流前置操作
     *
     * @param workflow  工作流执行数据
     */
    void  beforeBuildHandler(Workflow workflow);


    /**
     * 创建工作流后置操作
     *
     * @param workflow  工作流执行数据
     */
    void  afterBuildHandler(Workflow workflow);
}
