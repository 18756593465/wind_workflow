package com.bcx.wind.workflow.entity;

import com.bcx.wind.workflow.helper.JsonHelper;
import com.bcx.wind.workflow.helper.ObjectHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * 任务操作人实体
 *
 * @author zhanglei
 */
public class TaskActor {

    /**
     * 任务主键
     */
    private String taskId;

    /**
     * 任务操作人ID
     */
    private String taskActorId;

    /**
     * 审批人信息
     */
    private String actorVariable;


    public String getTaskId() {
        return taskId;
    }

    public TaskActor setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public String getTaskActorId() {
        return taskActorId;
    }

    public TaskActor setTaskActorId(String taskActorId) {
        this.taskActorId = taskActorId;
        return this;
    }

    public String getActorVariable() {
        return actorVariable;
    }

    public TaskActor setActorVariable(String actorVariable) {
        this.actorVariable = actorVariable;
        return this;
    }


    @SuppressWarnings("unchecked")
    public Map<String,Object> getActorVariableMap(){
        if(!ObjectHelper.isEmpty(this.actorVariable)){
            return JsonHelper.parseJson(this.actorVariable,Map.class,String.class,Object.class);
        }

        return new HashMap<>();
    }



}
