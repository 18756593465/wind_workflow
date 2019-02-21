package com.bcx.wind.workflow.core.pojo;

import java.util.LinkedList;
import java.util.List;

/**
 * 审批人实体
 *
 * @author zhanglei
 */
public class ApproveUser {

    /**
     * 任务名称 节点名称
     */
    private String nodeId;

    /**
     * 审批人集合
     */
    private List<DefaultUser>  approveUsers = new LinkedList<>();


    public String getNodeId() {
        return nodeId;
    }

    public ApproveUser setNodeId(String nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public List<DefaultUser> getApproveUsers() {
        return approveUsers;
    }

    public ApproveUser setApproveUsers(List<DefaultUser> approveUsers) {
        this.approveUsers = approveUsers;
        return this;
    }
}
