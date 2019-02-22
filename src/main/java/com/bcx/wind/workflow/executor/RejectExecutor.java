package com.bcx.wind.workflow.executor;

import com.bcx.wind.workflow.core.Actuator;
import com.bcx.wind.workflow.core.constant.NodeName;
import com.bcx.wind.workflow.core.flow.ScribeTaskNode;
import com.bcx.wind.workflow.core.flow.TaskModel;
import com.bcx.wind.workflow.core.flow.TaskNode;
import com.bcx.wind.workflow.core.pojo.Task;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.message.MessageHelper;

import java.util.List;

import static com.bcx.wind.workflow.message.MsgConstant.*;


/**
 * 退回执行
 *
 * @author zhanglei
 */
public class RejectExecutor extends BaseExecutor {


    public RejectExecutor(Actuator actuator) {
        super(actuator);
    }


    @Override
    public void executor() {
        //校验驳回节点
        addSubmitNode();
        //构建流程实例，获取当前任务
        buildWorkflow();
        //校验是否可以驳回
        checkCanReject();
        //执行提交（这里的提交可以理解为驳回）
        new SubmitExecutor(this.actuator).submit(variable());
    }


    /**
     * 校验当前节点和被驳回节点是否可以执行驳回操作
     */
    private void checkCanReject(){
        List<Task> taskList = workflow().getCurTask();
        for(Task task : taskList){
            TaskModel taskModel = task.getTaskModel();
            Assert.isTrue(MessageHelper.getMsg(w024),taskModel.name().equals(variable().getSubmitNode()));
            Assert.isTrue(MessageHelper.getMsg(w025),!this.actuator.getProcessModel().isLastTask(task.getTaskName(),variable().getSubmitNode()));
            Assert.isTrue(MessageHelper.getMsg(w021,taskModel.displayName(),NodeName.SUB_SCRIBE_TASK),taskModel instanceof ScribeTaskNode);

            if(taskModel instanceof TaskNode) {
                TaskNode node = (TaskNode)taskModel;
                Assert.isTrue(MessageHelper.getMsg(w021,node.displayName(),NodeName.FORK_TASK),node.isInAnd() || node.isInOr());
            }
        }
    }



    private void addSubmitNode(){
        String submitNode = variable().getSubmitNode();

        //校验驳回节点是否可以被驳回
        if(!ObjectHelper.isEmpty(submitNode)) {
            checkNodeCanBeReject(submitNode);
        }
        Assert.notEmpty(MessageHelper.getMsg(w023,""),submitNode);
        variable().setSubmitNode(submitNode);
    }


    private void checkNodeCanBeReject(String submitNode){
        TaskModel taskModel = actuator.getProcessModel().getTaskModel(submitNode);
        Assert.notEmpty(MessageHelper.getMsg(w023,submitNode),taskModel);

        Assert.isTrue(MessageHelper.getMsg(w021,taskModel.displayName(),NodeName.SUB_SCRIBE_TASK),taskModel instanceof ScribeTaskNode);
        if(taskModel instanceof TaskNode) {
            TaskNode node = (TaskNode)taskModel;
            Assert.isTrue(MessageHelper.getMsg(w022,node.displayName(),NodeName.FORK_TASK),node.isInAnd() || node.isInOr());
        }
    }



}
