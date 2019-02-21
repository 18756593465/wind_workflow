package com.bcx.wind.workflow;

import com.bcx.wind.workflow.core.pojo.Workflow;

/**
 * @author zhanglei
 */
public interface Interceptor {

    void  execute(Workflow workflow);
}
