package com.bcx.wind.workflow.core.pojo;


import com.bcx.wind.workflow.WorkflowEngine;
import com.bcx.wind.workflow.entity.OrderInstance;
import com.bcx.wind.workflow.entity.ProcessDefinition;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 工作流数据集合
 *
 * @author zhanglei
 */
public class Workflow {

    /**
     * 流程模型
     */
    private ProcessDefinition processDefinition;

    /**
     * 系统
     */
    private String system;

    /**
     * 流程实例号
     */
    private String orderId;


    /**
     * 业务ID
     */
    private List<String> businessId =new LinkedList<>();


    /**
     * 流程配置
     */
    private Configuration processConfig;


    /**
     * 当前任务
     */
    private List<Task>  curTask = new LinkedList<>();


    /**
     * 当前用户
     */
    private DefaultUser user;


    /**
     * 审批人
     */
    private List<ApproveUser> approveUsers = new LinkedList<>();


    /**
     * 工作流审批参数
     */
    private WorkflowVariable variable;

    /**
     * 流程实例
     */
    private OrderInstance orderInstance;


    private List<OrderInstance>  childOrderInstance = new LinkedList<>();

    /**
     * 业务数据
     */
    private Map<String,Object> dataMap;


    /**
     * 引擎
     */
    private WorkflowEngine engine;

    public ProcessDefinition getProcessDefinition() {
        return processDefinition;
    }

    public Workflow setProcessDefinition(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
        return this;
    }

    public String getSystem() {
        return system;
    }

    public Workflow setSystem(String system) {
        this.system = system;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public Workflow setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public Configuration getProcessConfig() {
        return processConfig;
    }

    public Workflow setProcessConfig(Configuration processConfig) {
        this.processConfig = processConfig;
        return this;
    }

    public List<Task> getCurTask() {
        return curTask;
    }

    public Workflow setCurTask(List<Task> curTask) {
        this.curTask = curTask;
        return this;
    }

    public DefaultUser getUser() {
        return user;
    }

    public Workflow setUser(DefaultUser user) {
        this.user = user;
        return this;
    }

    public List<ApproveUser> getApproveUsers() {
        return approveUsers;
    }

    public Workflow setApproveUsers(List<ApproveUser> approveUsers) {
        this.approveUsers = approveUsers;
        return this;
    }

    public List<String> getBusinessId() {
        return businessId;
    }

    public Workflow setBusinessId(List<String> businessId) {
        this.businessId = businessId;
        return this;
    }

    public WorkflowVariable getVariable() {
        return variable;
    }

    public Workflow setVariable(WorkflowVariable variable) {
        this.variable = variable;
        return this;
    }

    public OrderInstance getOrderInstance() {
        return orderInstance;
    }

    public Workflow setOrderInstance(OrderInstance orderInstance) {
        this.orderInstance = orderInstance;
        return this;
    }

    public List<OrderInstance> getChildOrderInstance() {
        return childOrderInstance;
    }

    public Workflow setChildOrderInstance(List<OrderInstance> childOrderInstance) {
        this.childOrderInstance = childOrderInstance;
        return this;
    }


    public WorkflowEngine getEngine() {
        return engine;
    }

    public Workflow setEngine(WorkflowEngine engine) {
        this.engine = engine;
        return this;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public Workflow setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
        return this;
    }
}
