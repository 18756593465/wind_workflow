package com.bcx.wind.workflow.entity;

/**
 * 完结历史实体
 * @author zhanglei
 */
public class CompleteHistory  {


    /**
     * 历史主键
     */
    private String id;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务显示名称
     */
    private String taskDisplayName;

    /**
     * 模型ID
     */
    private String processId;

    /**
     * 流程实例ID
     */
    private String orderId;

    /**
     * 模型名称
     */
    private String processName;

    /**
     * 模型显示名称
     */
    private String processDisplayName;

    /**
     * 操作
     */
    private String operate;

    /**
     * 建议
     */
    private String suggest;

    /**
     * 审批时间
     */
    private String approveTime;

    /**
     * 操作人ID
     */
    private String actorId;


    /**
     * 操作人名称
     */
    private String actorName;

    /**
     * 审批人ID
     */
    private String approveId;

    /**
     * 审批人名称
     */
    private String approveName;

    /**
     * 历史履历创建时间
     */
    private String createTime;

    /**
     * 审批人数据
     */
    private String approveUserVariable;

    /**
     * 任务类型
     */
    private String taskType;

    //系统
    private String system;

    //实际提交人
    private String submitUserVariable;

    public String getId() {
        return id;
    }

    public CompleteHistory setId(String id) {
        this.id = id;
        return this;
    }

    public String getTaskId() {
        return taskId;
    }

    public CompleteHistory setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public String getTaskName() {
        return taskName;
    }

    public CompleteHistory setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public String getTaskDisplayName() {
        return taskDisplayName;
    }

    public CompleteHistory setTaskDisplayName(String taskDisplayName) {
        this.taskDisplayName = taskDisplayName;
        return this;
    }

    public String getProcessId() {
        return processId;
    }

    public CompleteHistory setProcessId(String processId) {
        this.processId = processId;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public CompleteHistory setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }


    public String getProcessName() {
        return processName;
    }

    public CompleteHistory setProcessName(String processName) {
        this.processName = processName;
        return this;
    }

    public String getProcessDisplayName() {
        return processDisplayName;
    }

    public CompleteHistory setProcessDisplayName(String processDisplayName) {
        this.processDisplayName = processDisplayName;
        return this;
    }

    public String getOperate() {
        return operate;
    }

    public CompleteHistory setOperate(String operate) {
        this.operate = operate;
        return this;
    }

    public String getSuggest() {
        return suggest;
    }

    public CompleteHistory setSuggest(String suggest) {
        this.suggest = suggest;
        return this;
    }

    public String getApproveTime() {
        return approveTime;
    }

    public CompleteHistory setApproveTime(String approveTime) {
        this.approveTime = approveTime;
        return this;
    }

    public String getActorId() {
        return actorId;
    }

    public CompleteHistory setActorId(String actorId) {
        this.actorId = actorId;
        return this;
    }

    public String getActorName() {
        return actorName;
    }

    public CompleteHistory setActorName(String actorName) {
        this.actorName = actorName;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public CompleteHistory setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getApproveUserVariable() {
        return approveUserVariable;
    }

    public CompleteHistory setApproveUserVariable(String approveUserVariable) {
        this.approveUserVariable = approveUserVariable;
        return this;
    }

    public String getTaskType() {
        return taskType;
    }

    public CompleteHistory setTaskType(String taskType) {
        this.taskType = taskType;
        return this;
    }

    public String getSystem() {
        return system;
    }

    public CompleteHistory setSystem(String system) {
        this.system = system;
        return this;
    }

    public String getApproveId() {
        return approveId;
    }

    public CompleteHistory setApproveId(String approveId) {
        this.approveId = approveId;
        return this;
    }

    public String getApproveName() {
        return approveName;
    }

    public CompleteHistory setApproveName(String approveName) {
        this.approveName = approveName;
        return this;
    }

    public String getSubmitUserVariable() {
        return submitUserVariable;
    }

    public CompleteHistory setSubmitUserVariable(String submitUserVariable) {
        this.submitUserVariable = submitUserVariable;
        return this;
    }
}
