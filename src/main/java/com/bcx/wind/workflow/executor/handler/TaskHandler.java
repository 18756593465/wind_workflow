package com.bcx.wind.workflow.executor.handler;

import com.bcx.wind.workflow.core.Actuator;
import com.bcx.wind.workflow.core.constant.TaskStatus;
import com.bcx.wind.workflow.core.constant.TaskType;
import com.bcx.wind.workflow.core.constant.WorkflowOperateConstant;
import com.bcx.wind.workflow.core.flow.NodeModel;
import com.bcx.wind.workflow.core.flow.TaskModel;
import com.bcx.wind.workflow.core.flow.TaskNode;
import com.bcx.wind.workflow.core.pojo.ApproveUser;
import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.core.pojo.Task;
import com.bcx.wind.workflow.core.pojo.User;
import com.bcx.wind.workflow.entity.ActiveHistory;
import com.bcx.wind.workflow.entity.TaskInstance;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.JsonHelper;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.TimeHelper;
import com.bcx.wind.workflow.message.MessageHelper;

import java.util.*;

import static com.bcx.wind.workflow.core.constant.Constant.JSON;
import static com.bcx.wind.workflow.core.constant.OrderVariableKey.*;
import static com.bcx.wind.workflow.core.constant.WorkflowOperateConstant.BUILD;
import static com.bcx.wind.workflow.message.MsgConstant.w011;

/**
 * 任务
 *
 * @author zhanglei
 */
public class TaskHandler extends BaseHandler implements Handler{


    public TaskHandler(Actuator actuator, NodeModel now, Task task) {
        super(actuator, now, task);
    }

    @Override
    public void handler() {
        buildTask();
    }


    private  void   buildTask(){
        //审批人
        List<ApproveUser> approveUsers = workflow().getApproveUsers();

        if(BUILD.equals(actuator.getOperate().name())){
            TaskInstance instance = createNewTask(true);
            engine().runtimeService().taskService().createNewTask(instance);
            //添加履历
            createActiveHistory(false,instance.getId());

            addNowTaskInstance(instance);
            return;
        }

        boolean createNewTask = false;
        addApproveUser(approveUsers);

        Assert.notEmpty("submitWorkflow require parameters orderId user approveUsers has null!", approveUsers);
        for(ApproveUser user : approveUsers){
            String nodeId = user.getNodeId();
            if(nodeId.equals(this.now.name())) {
                createNewTask = true;
                List<DefaultUser> users = user.getApproveUsers();
                if (now instanceof TaskNode && ((TaskNode)now).isJointly()) {
                    //收集任务id集合
                    List<String> taskIds = new LinkedList<>();
                    for (User u : users) {
                        //任务
                        TaskInstance instance = createNewTask(true);
                        taskIds.add(instance.getId());

                        //创建任务和审批人
                        engine().runtimeService().taskService().createNewTask(instance);
                        engine().runtimeService().taskService().addActor(instance.getId(), u);

                        //添加履历
                        createActiveHistory(true,instance.getId());
                        addWorkflowNextTaskInstance(instance);
                    }
                    addRevokeTaskIdToOrder(taskIds);

                } else {
                    //任务
                    TaskInstance instance = createNewTask(false);

                    //创建任务和审批人
                    engine().runtimeService().taskService().createNewTask(instance);
                    engine().runtimeService().taskService().addActor(instance.getId(), users);

                    //添加履历
                    createActiveHistory(false,instance.getId());
                    //设置任务实例到执行对象中
                    addWorkflowNextTaskInstance(instance);

                    addRevokeTaskIdToOrder(Collections.singletonList(instance.getId()));
                }
            }
        }
        Assert.isTrue(MessageHelper.getMsg(w011),!createNewTask);
    }


    /**
     * 设置撤销信息  新的任务id集合
     *
     * @param taskIds  任务id集合
     */
    @SuppressWarnings("unchecked")
    private void addRevokeTaskIdToOrder(List<String> taskIds){
        if(ObjectHelper.isEmpty(this.task)){
            return;
        }
        TaskModel taskModel = this.task.getTaskModel();
        //分支中的任务不可撤销
        if(taskModel instanceof TaskNode && !((TaskNode) taskModel).isInAnd()
                && !((TaskNode) taskModel).isInOr()) {

            Object revokeMsg = workflow().getOrderInstance().getVariableMap().get(REVOKE_VARIABLE);
            if (!ObjectHelper.isEmpty(revokeMsg)) {
                Map<String, Object> args = JsonHelper.coverObject(revokeMsg, Map.class, String.class, Object.class);
                args.put(NEW_TASK_ID, taskIds);
                workflow().getOrderInstance().addValue(REVOKE_VARIABLE, args);
            } else {
                Map<String, Object> args = new HashMap<>();
                args.put(NEW_TASK_ID, taskIds);
                workflow().getOrderInstance().addValue(REVOKE_VARIABLE, args);
            }
        }
    }


    @SuppressWarnings("unchecked")
    private List<ApproveUser> addApproveUser(List<ApproveUser> approveUsers){
        if(ObjectHelper.isEmpty(approveUsers) && WorkflowOperateConstant.REJECT.equals(actuator.getOperate().name())){
            Object userObjs = workflow().getOrderInstance().getVariableMap().get(TASK_APPROVE_USER);

            if (!ObjectHelper.isEmpty(userObjs)) {
                Map<String,List<DefaultUser>> userMaps = JsonHelper.coverObject(userObjs,Map.class,String.class,List.class);

                for(Map.Entry<String,List<DefaultUser>> user : userMaps.entrySet()){
                    String nodeId = user.getKey();
                    List<DefaultUser> users = user.getValue();
                    users = JsonHelper.coverObject(users,List.class,DefaultUser.class);
                    approveUsers.add(new ApproveUser().setNodeId(nodeId).setApproveUsers(users));
                }
            }
        }

        return  approveUsers;
    }



    private void addNowTaskInstance(TaskInstance instance) {
        if(!ObjectHelper.isEmpty(this.task)) {
            this.task.setTaskInstance(instance);
        }
    }


    /**
     * 设置流程执行参数
     * @param instance  新的任务
     */
    private void  addWorkflowNextTaskInstance(TaskInstance instance){
        //添加到流程执行参数中
        if(!ObjectHelper.isEmpty(this.task)) {
            List<Task> nextTasks = this.task.getNextTask();
            for (Task t : nextTasks) {
                if (t.getTaskName().equals(this.now.name())) {
                    t.setTaskInstance(instance);
                }
            }
        }
    }



    private void createActiveHistory(boolean jointly,String taskId){
        ActiveHistory history = new ActiveHistory()
                .setProcessName(this.actuator.getProcessModel().getName())
                .setProcessDisplayName(this.actuator.getProcessModel().getDisplayName())
                .setApproveUserVariable(JsonHelper.toJson(workflow().getApproveUsers()))
                .setTaskType(jointly ? TaskType.ALL : TaskType.ANY)
                .setSystem(workflow().getSystem())
                .setTaskId(taskId)
                .setTaskName(this.now.name())
                .setTaskDisplayName(this.now.displayName())
                .setProcessId(this.actuator.getProcessModel().getProcessId())
                .setOrderId(workflow().getOrderId());

        engine().historyService().activeHistoryService().insert(history);
    }

    private TaskInstance createNewTask(boolean jointly){
        //任务
        return  new TaskInstance()
                .setOrderId(workflow().getOrderId())
                .setApproveCount(0)
                .setCreateTime(TimeHelper.getNow())
                .setId(ObjectHelper.primaryKey())
                .setVersion(0)
                .setVariable(JSON)
                .setDisplayName(this.now.displayName())
                .setTaskName(this.now.name())
                .setProcessId(this.actuator.getProcessModel().getProcessId())
                .setStatus(TaskStatus.RUN)
                .setTaskType(jointly ? TaskType.ALL : TaskType.ANY)
                .setPosition(nodeConfig().getTaskLink());
    }

    public TaskNode getTaskNode() {
        return (TaskNode) now;
    }

    public TaskHandler setTaskNode(TaskNode taskNode) {
        this.now = taskNode;
        return this;
    }

    public Task getTask() {
        return task;
    }

    public TaskHandler setTask(Task task) {
        this.task = task;
        return  this;
    }

}
