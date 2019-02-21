package com.bcx.wind.workflow.core.pojo;

/**
 * 节点配置
 *
 * @author zhanglei
 */
public class NodeConfig {

    /**
     * 退回节点
     */
    private  String  returnNode;


    /**
     * 提交路线
     */
    private  String  submitLine;


    /**
     * 任务内容
     */
    private  String  taskContent;


    /**
     * 任务链接
     */
    private  String  taskLink;


    /**
     * 会签分支审批人合并配置 1合并 0或不设置表示不和并  默认不合并
     */
    private  int  jointlyApproveUserSet = 0;


    /**
     * 转办任务次数配置 设置该设置后，在转办任务达到设置上限时，则不允许进行转办   默认可以转办
     */
    private  int  transferTimeSet;


    /**
     * 会签审批通过率配置  会签任务通过人数达到通过率标准则审批通过。为小数0.1-1
     */
    private  String  jointlyApproveTimeSet;

    /**
     * 会签审批通过最少人次  和上面的通过率配置相比  优先级第一点，如果配置了审批率则优先用审批率进行判断，忽略最小审批人次数配置
     */
    private  int  jointlyApproveUserMinCount;

    /**
     * 配置是否为会签  1是   -1否  如果没有配置则是0  采用流程中的设置
     */
    private  int  jointly;


    /**
     * 是否可以自由提交 1是  0否
     */
    private  int  freeFlowSet;


    /**
     * 是否发送邮件 默认不发送  1 发送
     */
    private  int  emailSet;

    public String getReturnNode() {
        return returnNode;
    }

    public NodeConfig setReturnNode(String returnNode) {
        this.returnNode = returnNode;
        return this;
    }

    public String getSubmitLine() {
        return submitLine;
    }

    public NodeConfig setSubmitLine(String submitLine) {
        this.submitLine = submitLine;
        return this;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public NodeConfig setTaskContent(String taskContent) {
        this.taskContent = taskContent;
        return this;
    }

    public String getTaskLink() {
        return taskLink;
    }

    public NodeConfig setTaskLink(String taskLink) {
        this.taskLink = taskLink;
        return this;
    }

    public int getJointlyApproveUserSet() {
        return jointlyApproveUserSet;
    }

    public NodeConfig setJointlyApproveUserSet(int jointlyApproveUserSet) {
        this.jointlyApproveUserSet = jointlyApproveUserSet;
        return this;
    }

    public int getTransferTimeSet() {
        return transferTimeSet;
    }

    public NodeConfig setTransferTimeSet(int transferTimeSet) {
        this.transferTimeSet = transferTimeSet;
        return this;
    }

    public String getJointlyApproveTimeSet() {
        return jointlyApproveTimeSet;
    }

    public NodeConfig setJointlyApproveTimeSet(String jointlyApproveTimeSet) {
        this.jointlyApproveTimeSet = jointlyApproveTimeSet;
        return this;
    }

    public int getEmailSet() {
        return emailSet;
    }

    public NodeConfig setEmailSet(int emailSet) {
        this.emailSet = emailSet;
        return this;
    }

    public int getJointlyApproveUserMinCount() {
        return jointlyApproveUserMinCount;
    }

    public NodeConfig setJointlyApproveUserMinCount(int jointlyApproveUserMinCount) {
        this.jointlyApproveUserMinCount = jointlyApproveUserMinCount;
        return this;
    }

    public int getJointly() {
        return jointly;
    }

    public NodeConfig setJointly(int jointly) {
        this.jointly = jointly;
        return this;
    }

    public int getFreeFlowSet() {
        return freeFlowSet;
    }

    public NodeConfig setFreeFlowSet(int freeFlowSet) {
        this.freeFlowSet = freeFlowSet;
        return this;
    }
}
