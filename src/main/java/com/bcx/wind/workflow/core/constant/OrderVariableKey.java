package com.bcx.wind.workflow.core.constant;

/**
 * 流程实例变量键
 *
 * @author zhanglei
 */
public class OrderVariableKey {

    //提交当前任务的审批人数据  用于后期退回操作的审批人
    public static final String TASK_APPROVE_USER = "taskApproveUser";


    //上一次被提交的任务节点
    public static final String LAST_SUBMIT_TASK_NODE = "lastTaskSubmitTaskNode";


    //保存撤销任务节点信息
    public static final String REVOKE_VARIABLE = "revokeVariable";


    public static final String REVOKE_NODE = "revokeNode";

    public static final String NEW_TASK_ID = "newTaskId";

}
