package com.bcx.wind.workflow.core.handler;

import com.bcx.wind.workflow.core.pojo.Workflow;

/**
 * 订阅拦截器
 *
 * @author zhanglei
 */
public interface SubScribeHandler {


    void  beforeSubScribeHandler(Workflow workflow);



    void  afterSubScribeHandler(Workflow workflow);
}
