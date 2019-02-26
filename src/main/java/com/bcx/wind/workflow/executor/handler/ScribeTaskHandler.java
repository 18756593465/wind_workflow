package com.bcx.wind.workflow.executor.handler;

import com.bcx.wind.workflow.core.Actuator;
import com.bcx.wind.workflow.core.constant.TaskStatus;
import com.bcx.wind.workflow.core.constant.TaskType;
import com.bcx.wind.workflow.core.constant.WorkflowOperate;
import com.bcx.wind.workflow.core.flow.NodeModel;
import com.bcx.wind.workflow.core.pojo.ApproveUser;
import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.core.pojo.Task;
import com.bcx.wind.workflow.entity.ActiveHistory;
import com.bcx.wind.workflow.entity.TaskInstance;
import com.bcx.wind.workflow.helper.JsonHelper;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.TimeHelper;

import java.util.List;
import java.util.stream.Collectors;

import static com.bcx.wind.workflow.core.constant.Constant.JSON;

/**
 * 订阅任务处理器
 */
public class ScribeTaskHandler extends BaseHandler implements Handler {


    public ScribeTaskHandler(Actuator actuator, NodeModel now, Task task) {
        super(actuator, now, task);
    }

    @Override
    public void handler() {
        buildScribeTask();
    }


    private void buildScribeTask(){

        List<ApproveUser> approveUsers =  workflow().getApproveUsers();
        if(!ObjectHelper.isEmpty(approveUsers)){
            for(ApproveUser user : approveUsers){

                String nodeId = user.getNodeId();
                if(now.name().equals(nodeId)){
                    //创建任务
                    TaskInstance instance = createScribeTask();
                    engine().runtimeService().taskService().createNewTask(instance);
                    //审批人
                    engine().runtimeService().taskService().addActor(instance.getId(),user.getApproveUsers());

                    //历史履历
                    ActiveHistory history = createActiveHistory(instance,approveUsers);
                    engine().historyService().activeHistoryService().insert(history);

                }
            }

        }
    }


    private ActiveHistory createActiveHistory(TaskInstance instance,List<ApproveUser> approveUsers){
        return new ActiveHistory()
                .setTaskId(instance.getId())
                .setTaskName(instance.getTaskName())
                .setTaskDisplayName(instance.getDisplayName())
                .setProcessId(actuator.getProcessModel().getProcessId())
                .setOrderId(workflow().getOrderId())
                .setProcessName(actuator.getProcessModel().getName())
                .setProcessDisplayName(actuator.getProcessModel().getDisplayName())
                .setOperate(WorkflowOperate.scribe.value())
                .setApproveUserVariable(JsonHelper.toJson(approveUsers))
                .setSystem(workflow().getSystem())
                .setTaskType(TaskType.SCRIBE);
    }


    private TaskInstance createScribeTask(){
        return new TaskInstance()
                .setId(ObjectHelper.primaryKey())
                .setTaskName(now.name())
                .setDisplayName(now.displayName())
                .setTaskType(TaskType.SCRIBE)
                .setCreateTime(TimeHelper.getNow())
                .setApproveCount(0)
                .setStatus(TaskStatus.RUN)
                .setOrderId(workflow().getOrderId())
                .setProcessId(actuator.getProcessModel().getProcessId())
                .setVariable(JSON)
                .setVersion(1)
                .setPosition(nodeConfig().getTaskLink());
    }

    public Actuator getActuator() {
        return actuator;
    }

    public ScribeTaskHandler setActuator(Actuator actuator) {
        this.actuator = actuator;
        return this;
    }

    public NodeModel getNow() {
        return now;
    }

    public ScribeTaskHandler setNow(NodeModel now) {
        this.now = now;
        return this;
    }

    public Task getTask() {
        return task;
    }

    public ScribeTaskHandler setTask(Task task) {
        this.task = task;
        return this;
    }
}
