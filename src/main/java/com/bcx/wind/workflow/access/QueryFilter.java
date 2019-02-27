package com.bcx.wind.workflow.access;

import com.bcx.wind.workflow.helper.Assert;

import java.util.List;

/**
 * 查询过滤条件
 *
 * @author zhanglei
 */
public class QueryFilter {

    /**
     * 流程定义ID
     */
    private String  processId;

    /**
     * 流程定义名称
     */
    private String  processName;

    /**
     * 流程实例号
     */
    private String  orderId;

    /**
     * 流程实例号数组
     */
    private String[] orderIds;

    /**
     * 任务ID
     */
    private String  taskId;

    /**
     * 任务id数组
     */
    private String[] taskIds;

    /**
     * 任务操作人ID
     */
    private String[]  taskActorId;

    /**
     * 任务审批人ID
     */
    private String[]  taskApproveId;


    /**
     * 业务数据主键
     */
    private List<String>  businessId;


    /**
     * 节点ID
     */
    private String  nodeId;


    /**
     * 任务名称
     */
    private String taskName;


    /**
     * 排序
     */
    private String  orderBy;

    /**
     * 扩展数据
     */
    private String  variable;

    /**
     * 版本
     */
    private int version;

    /**
     * 创建时间开始
     */
    private String createTimeStart;

    /**
     * 创建时间结束
     */
    private String createTimeEnd;

    /**
     * 父ID
     */
    private String parentId;


    /**
     * 状态
     */
    private String status;


    /**
     * 任务类型
     */
    private String taskType;


    /**
     * 系统
     */
    private String system;


    /**
     * 任务地址
     */
    private String position;

    public String getPosition() {
        return position;
    }

    public QueryFilter setPosition(String position) {
        this.position = position;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public QueryFilter setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getProcessId() {
        return processId;
    }

    public QueryFilter setProcessId(String processId) {
        Assert.nonEmpty(processId);
        this.processId = processId;
        return this;
    }

    public String getProcessName() {
        return processName;
    }

    public QueryFilter setProcessName(String processName) {
        Assert.nonEmpty(processName);
        this.processName = processName;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public QueryFilter setOrderId(String orderId) {
        Assert.nonEmpty(orderId);
        this.orderId = orderId;
        return this;
    }

    public String getTaskId() {
        return taskId;
    }

    public QueryFilter setTaskId(String taskId) {
        Assert.nonEmpty(taskId);
        this.taskId = taskId;
        return this;
    }


    public String[] getTaskActorId() {
        return taskActorId;
    }


    public QueryFilter setTaskActorId(String[] taskActorId) {
        Assert.nonEmpty(taskActorId);
        this.taskActorId = taskActorId;
        return this;
    }


    public List<String> getBusinessId() {
        return businessId;
    }

    public QueryFilter setBusinessId(List<String> businessId) {
        this.businessId = businessId;
        return this;
    }

    public String getNodeId() {
        return nodeId;
    }

    public QueryFilter setNodeId(String nodeId) {
        Assert.nonEmpty(nodeId);
        this.nodeId = nodeId;
        return this;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public QueryFilter setOrderBy(String orderBy) {
        Assert.nonEmpty(orderBy);
        this.orderBy = orderBy;
        return this;
    }

    public String getVariable() {
        return variable;
    }

    public QueryFilter setVariable(String variable) {
        Assert.nonEmpty(variable);
        this.variable = variable;
        return this;
    }

    public String getTaskName() {
        return taskName;
    }

    public QueryFilter setTaskName(String taskName) {
        Assert.nonEmpty(taskName);
        this.taskName = taskName;
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public QueryFilter setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public String getCreateTimeStart() {
        return createTimeStart;
    }

    public QueryFilter setCreateTimeStart(String createTimeStart) {
        Assert.nonEmpty(createTimeStart);
        this.createTimeStart = createTimeStart;
        return this;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public QueryFilter setCreateTimeEnd(String createTimeEnd) {
        Assert.nonEmpty(createTimeEnd);
        this.createTimeEnd = createTimeEnd;
        return this;
    }

    public String getParentId() {
        return parentId;
    }

    public QueryFilter setParentId(String parentId) {
        Assert.nonEmpty(parentId);
        this.parentId = parentId;
        return this;
    }

    public String getSystem() {
        return system;
    }

    public QueryFilter setSystem(String system) {
        Assert.nonEmpty(system);
        this.system = system;
        return this;
    }

    public String[] getTaskApproveId() {
        return taskApproveId;
    }

    public QueryFilter setTaskApproveId(String[] taskApproveId) {
        Assert.nonEmpty(taskApproveId);
        this.taskApproveId = taskApproveId;
        return this;
    }

    public String[] getTaskIds() {
        return taskIds;
    }

    public QueryFilter setTaskIds(String[] taskIds) {
        this.taskIds = taskIds;
        return this;
    }

    public String getTaskType() {
        return taskType;
    }

    public QueryFilter setTaskType(String taskType) {
        this.taskType = taskType;
        return this;
    }

    public String[] getOrderIds() {
        return orderIds;
    }

    public QueryFilter setOrderIds(String[] orderIds) {
        this.orderIds = orderIds;
        return this;
    }
}
