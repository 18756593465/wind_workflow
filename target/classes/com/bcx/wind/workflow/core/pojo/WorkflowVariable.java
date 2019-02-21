package com.bcx.wind.workflow.core.pojo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 流程操作参数对象
 * 
 * @author zhanglei
 */
public class WorkflowVariable {

    /**
     * 当前用户
     */
    private DefaultUser user;

    /**
     * 流程实例号  可无
     */
    private String orderId;

    /**
     * 业务ID
     */
    private List<String> businessId;


    /**
     * 审批人信息
     */
    private List<ApproveUser>  approveUsers = new LinkedList<>();


    /**
     * 模型名称
     */
    private String processName;


    /**
     * 模型ID 可无
     */
    private String processId;

    /**
     * 建议
     */
    private String suggest;


    /**
     * 提交路线
     */
    private Map<String,String> path = new HashMap<>();


    /**
     * 业务数据
     */
    private Map<String,Object> dataMap = new HashMap<>();

    /**
     * 指定提交节点，不管流程前后
     */
    private String submitNode;

    //业务系统
    private String system;


    public DefaultUser getUser() {
        return user;
    }

    public WorkflowVariable setUser(DefaultUser user) {
        this.user = user;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public WorkflowVariable setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public List<String> getBusinessId() {
        return businessId;
    }

    public WorkflowVariable setBusinessId(List<String> businessId) {
        this.businessId = businessId;
        return this;
    }

    public List<ApproveUser> getApproveUsers() {
        return approveUsers;
    }

    public WorkflowVariable setApproveUsers(List<ApproveUser> approveUsers) {
        this.approveUsers = approveUsers;
        return this;
    }

    public String getProcessName() {
        return processName;
    }

    public WorkflowVariable setProcessName(String processName) {
        this.processName = processName;
        return this;
    }

    public String getProcessId() {
        return processId;
    }

    public WorkflowVariable setProcessId(String processId) {
        this.processId = processId;
        return this;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public WorkflowVariable setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
        return this;
    }

    public String getSuggest() {
        return suggest;
    }

    public WorkflowVariable setSuggest(String suggest) {
        this.suggest = suggest;
        return this;
    }

    public Map<String, String> getPath() {
        return path;
    }

    public WorkflowVariable setPath(Map<String, String> path) {
        this.path = path;
        return this;
    }

    public String getSystem() {
        return system;
    }

    public WorkflowVariable setSystem(String system) {
        this.system = system;
        return this;
    }

    public String getSubmitNode() {
        return submitNode;
    }

    public WorkflowVariable setSubmitNode(String submitNode) {
        this.submitNode = submitNode;
        return this;
    }
}
