package com.bcx.wind.workflow.core.flow;

import com.bcx.wind.workflow.core.constant.NodeType;
import com.bcx.wind.workflow.entity.TaskInstance;

/**
 * 订阅任务
 *
 * @author zhanglei
 */
public class ScribeTaskNode extends BaseNode implements TaskModel{

    //是否拦截
    private boolean interceptor;

    public ScribeTaskNode(String name, String displayName) {
        super(name, displayName);
        this.nodeType = NodeType.SCRIBE_TASK;
    }

    public void build(){
        //构建路线
        buildPaths(now);
    }

    public boolean isInterceptor() {
        return interceptor;
    }


    public void setInterceptor(boolean interceptor) {
        this.interceptor = interceptor;
    }


    @Override
    public void executor() {
        TaskInstance instance = this.task.getTaskInstance();
        //删除抄送订阅任务
        engine().runtimeService().taskService().removeById(instance.getId());
        //删除抄送人
        engine().runtimeService().taskService().removeActorByTaskId(instance.getId());
    }

    @Override
    public NodeModel parentNode() {
        return this.parentNode;
    }
}
