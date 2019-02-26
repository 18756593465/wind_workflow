package com.bcx.wind.workflow.core.handler;

import com.bcx.wind.workflow.core.pojo.Workflow;

/**
 * 撤销前后置处理
 *
 * @author zhanglei
 */
public interface RevokeHandler {


    void  beforeRevokeHandler(Workflow workflow);



    void  afterRevokeHandler(Workflow  workflow);
}
