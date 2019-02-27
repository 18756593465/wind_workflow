package com.bcx.wind.workflow.core.pojo;

import com.bcx.wind.workflow.entity.TaskInstance;

import java.util.LinkedList;
import java.util.List;

/**
 * 转办结果对象
 *
 * @author zhanglei
 */
public class TransferResult {

    /**
     * 转办是否成功
     */
    private int code = 0;

    /**
     * 转办成功数量
     */
    private int count = 0;

    /**
     * 被转办的任务实例集合
     */
    private List<TaskInstance>  taskInstances = new LinkedList<>();

    public void addCount(){
        this.count++;
    }

    public int getCode() {
        return code;
    }

    public TransferResult setCode(int code) {
        this.code = code;
        return this;
    }

    public int getCount() {
        return count;
    }

    public TransferResult setCount(int count) {
        this.count = count;
        return this;
    }

    public List<TaskInstance> getTaskInstances() {
        return taskInstances;
    }

    public TransferResult setTaskInstances(List<TaskInstance> taskInstances) {
        this.taskInstances = taskInstances;
        return this;
    }
}
