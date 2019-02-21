package com.bcx.wind.workflow.entity;

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
}
