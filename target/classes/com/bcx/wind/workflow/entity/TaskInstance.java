package com.bcx.wind.workflow.entity;

import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.JsonHelper;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 任务实体
 *
 * @author zhanglei
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskInstance {

    /**
     * 任务实例主键
     */
    private String id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 显示名称
     */
    private String displayName;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 审批人
     */
    private String approveUser;

    /**
     * 创建人
     */
    private String createTime;

    /**
     * 审批人数量
     */
    private int approveCount;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 流程实例号
     */
    private String orderId;

    /**
     * 流程定义ID
     */
    private String processId;

    /**
     * 扩展数据
     */
    private String variable;

    /**
     * 父任务ID
     */
    private String parentId;

    /**
     * 版本
     */
    private int version;

    /**
     * 地址
     */
    private String position;



    /**
     * 任务变量集合
     */
    private Map<String,Object> variableMap = new HashMap<>();

    public void addVariable(String key,Object value){
        Assert.hasEmpty("data is empty!",key,value);
        this.variableMap.put(key,value);

        this.variable = JsonHelper.toJson(this.variableMap);
    }

    public void removeKey(String key){
        Assert.hasEmpty("key is empty!",key);

        if(this.variableMap.containsKey(key)){
            this.variableMap.remove(key);
            this.variable = JsonHelper.toJson(this.variableMap);
        }
    }

    public Map<String, Object> getVariableMap() {
        if(!ObjectHelper.isEmpty(this.variableMap)){
            return this.variableMap;
        }

        if(!ObjectHelper.isEmpty(this.variable)){
            this.variableMap = JsonHelper.jsonToMap(this.variable);
        }


        return variableMap;
    }

    public TaskInstance setVariableMap(Map<String, Object> variableMap) {
        this.variableMap = variableMap;
        return this;
    }

    public String getPosition() {
        return position;
    }

    public TaskInstance setPosition(String position) {
        this.position = position;
        return this;
    }

    public String getId() {
        return id;
    }

    public TaskInstance setId(String id) {
        this.id = id;
        return this;
    }

    public String getTaskName() {
        return taskName;
    }

    public TaskInstance setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public TaskInstance setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getTaskType() {
        return taskType;
    }

    public TaskInstance setTaskType(String taskType) {
        this.taskType = taskType;
        return this;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public TaskInstance setApproveUser(String approveUser) {
        this.approveUser = approveUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public TaskInstance setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public int getApproveCount() {
        return approveCount;
    }

    public TaskInstance setApproveCount(int approveCount) {
        this.approveCount = approveCount;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public TaskInstance setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public TaskInstance setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getProcessId() {
        return processId;
    }

    public TaskInstance setProcessId(String processId) {
        this.processId = processId;
        return this;
    }

    public String getVariable() {
        return variable;
    }

    public TaskInstance setVariable(String variable) {
        this.variable = variable;
        this.variableMap.clear();
        return this;
    }

    public String getParentId() {
        return parentId;
    }

    public TaskInstance setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    public int getVersion() {
        return version;
    }

    public TaskInstance setVersion(int version) {
        this.version = version;
        return this;
    }

}
