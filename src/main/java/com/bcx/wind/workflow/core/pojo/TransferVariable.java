package com.bcx.wind.workflow.core.pojo;

import java.util.LinkedList;
import java.util.List;

/**
 * 转办参数对象
 *
 * @author zhanglei
 */
public class TransferVariable {

    /**
     * 系统
     */
    private String system;

    /**
     * 流程定义ID
     */
    private String processId;

    /**
     * 流程实例ID
     */
    private String orderId;

    /**
     * 业务ID集合
     */
    private List<String> businessIds = new LinkedList<>();

    /**
     * 任务实例ID
     */
    private String taskId;

    /**
     * 旧审批人
     */
    private DefaultUser oldUser;

    /**
     * 新审批人
     */
    private DefaultUser newUser;


    /**
     * 当前用户
     */
    private DefaultUser user;


    public String getSystem() {
        return system;
    }

    public TransferVariable setSystem(String system) {
        this.system = system;
        return this;
    }

    public String getProcessId() {
        return processId;
    }

    public TransferVariable setProcessId(String processId) {
        this.processId = processId;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public TransferVariable setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public List<String> getBusinessIds() {
        return businessIds;
    }

    public TransferVariable setBusinessIds(List<String> businessIds) {
        this.businessIds = businessIds;
        return this;
    }

    public String getTaskId() {
        return taskId;
    }

    public TransferVariable setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public DefaultUser getOldUser() {
        return oldUser;
    }

    public TransferVariable setOldUser(DefaultUser oldUser) {
        this.oldUser = oldUser;
        return this;
    }

    public DefaultUser getNewUser() {
        return newUser;
    }

    public TransferVariable setNewUser(DefaultUser newUser) {
        this.newUser = newUser;
        return this;
    }

    public DefaultUser getUser() {
        return user;
    }

    public TransferVariable setUser(DefaultUser user) {
        this.user = user;
        return this;
    }
}
