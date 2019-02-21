package com.bcx.wind.workflow.executor.handler;

import com.bcx.wind.workflow.WorkflowEngine;
import com.bcx.wind.workflow.core.Actuator;
import com.bcx.wind.workflow.core.flow.NodeModel;
import com.bcx.wind.workflow.core.pojo.*;
import com.bcx.wind.workflow.helper.ObjectHelper;

import java.util.List;

public abstract class BaseHandler {

    protected Actuator actuator;

    /**
     * 当前子流程节点
     */
    protected NodeModel now;

    /**
     * 当前任务
     */
    protected Task task;

    public BaseHandler(Actuator actuator,NodeModel now,Task task){
        this.actuator = actuator;
        this.now = now;
        this.task = task;
    }


    public Actuator getActuator() {
        return actuator;
    }

    public BaseHandler setActuator(Actuator actuator) {
        this.actuator = actuator;
        return this;
    }

    public NodeModel getNow() {
        return now;
    }

    public BaseHandler setNow(NodeModel now) {
        this.now = now;
        return this;
    }

    public Task getTask() {
        return task;
    }

    public BaseHandler setTask(Task task) {
        this.task = task;
        return this;
    }

    protected Workflow workflow(){
        return this.actuator.getWorkflow();
    }


    protected DefaultUser user(){
        return workflow().getUser();
    }


    protected WorkflowEngine engine(){
        return this.actuator.getEngine();
    }

    protected NodeConfig nodeConfig(){
        if(!ObjectHelper.isEmpty(this.task)) {
            List<Task> nextTasks = this.task.getNextTask();
            for(Task next : nextTasks) {
                Configuration configuration = next.getTaskConfig();
                if (!ObjectHelper.isEmpty(configuration)) {
                    return configuration.getNodeConfig();
                }
            }
        }
        return new NodeConfig();
    }
}
