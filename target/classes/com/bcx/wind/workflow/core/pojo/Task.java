package com.bcx.wind.workflow.core.pojo;

import com.bcx.wind.workflow.core.flow.TaskModel;
import com.bcx.wind.workflow.core.flow.TaskNode;
import com.bcx.wind.workflow.entity.TaskInstance;

import java.util.LinkedList;
import java.util.List;

/**
 * 任务节点实例
 */
public class Task {

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务节点模型
     */
    private TaskModel taskModel;


    /**
     * 后续任务节点
     */
    private List<Task> nextTask = new LinkedList<>();

    /**
     * 流程ID
     */
    private String processId;


    /**
     * 任务实例
     */
    private TaskInstance taskInstance;


    /**
     * 任务配置
     */
    private Configuration taskConfig;

    public TaskModel getTaskModel() {
        return taskModel;
    }

    public Task setTaskModel(TaskModel taskModel) {
        this.taskModel = taskModel;
        return this;
    }

    public String getProcessId() {
        return processId;
    }

    public Task setProcessId(String processId) {
        this.processId = processId;
        return this;
    }

    public Configuration getTaskConfig() {
        return taskConfig;
    }

    public Task setTaskConfig(Configuration taskConfig) {
        this.taskConfig = taskConfig;
        return this;
    }

    public List<Task> getNextTask() {
        return nextTask;
    }

    public Task setNextTask(List<Task> nextTask) {
        this.nextTask = nextTask;
        return this;
    }

    public TaskInstance getTaskInstance() {
        return taskInstance;
    }

    public Task setTaskInstance(TaskInstance taskInstance) {
        this.taskInstance = taskInstance;
        return this;
    }

    public String getTaskName() {
        return taskName;
    }

    public Task setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }
}
