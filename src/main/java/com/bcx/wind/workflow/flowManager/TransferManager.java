package com.bcx.wind.workflow.flowManager;

import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.constant.WorkflowOperate;
import com.bcx.wind.workflow.core.flow.ProcessModel;
import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.core.pojo.ManagerData;
import com.bcx.wind.workflow.core.pojo.TransferResult;
import com.bcx.wind.workflow.core.pojo.TransferVariable;
import com.bcx.wind.workflow.entity.*;
import com.bcx.wind.workflow.helper.JsonHelper;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.TimeHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bcx.wind.workflow.core.constant.Constant.WIND_ADMIN;

/**
 * 转办实现
 *
 * @author zhanglei
 */
public class TransferManager  extends BaseManager  {

    private TransferVariable variable;

    /**
     * 转办结果
     */
    private TransferResult result = new TransferResult();


    public TransferManager(ManagerData managerData) {
        super(managerData);
    }

    /**
     * 转办任务逻辑
     * 1、查出需要转办的所有任务
     * 2、删除旧任务及审批人，更新旧任务履历为转办操作
     * 3、添加新任务及新审批人，添加新任务的了历史履历
     * 转办条件优先级  taskId > orderId > businessIds > processId > system > 所有
     *
     * @return   转办结果
     */
    @Override
    public TransferResult transfer() {
        if(!ObjectHelper.isEmpty(this.variable)){
            DefaultUser oldUser = this.variable.getOldUser();
            DefaultUser newUser = this.variable.getNewUser();

            if(!ObjectHelper.isEmpty(this.variable.getTaskId())){
                transferByTaskId(this.variable.getTaskId(),oldUser,newUser);
            }else if(!ObjectHelper.isEmpty(this.variable.getOrderId())){
                transferByOrderId(this.variable.getOrderId(),oldUser,newUser);
            }else if(!ObjectHelper.isEmpty(this.variable.getBusinessIds())){
                transferByBusinessId(this.variable.getBusinessIds(),oldUser,newUser);
            }else if(!ObjectHelper.isEmpty(this.variable.getProcessId())){
                transferByProcessId(this.variable.getProcessId(),oldUser,newUser);
            }else if(!ObjectHelper.isEmpty(this.variable.getSystem())){
                transferBySystem(this.variable.getSystem(),oldUser,newUser);
            }else{
                transfer(oldUser,newUser);
            }
        }
        return this.result;
    }


    /**
     * 转办操作
     *
     * @param taskIds  需要转办的任务ID
     * @param oldUser  旧审批人
     * @param newUser  新审批人
     */
    private void transferOperate(List<String> taskIds , DefaultUser oldUser ,DefaultUser newUser){

        if(!ObjectHelper.isEmpty(taskIds)){

            for(String taskId : taskIds){
                //任务实例
                TaskInstance instance = engine().runtimeService().taskService().getTaskById(taskId);
                if(ObjectHelper.isEmpty(instance)){
                    continue;
                }
                //删除任务实例
                engine().runtimeService().taskService().removeById(taskId);
                //删除任务审批人
                engine().runtimeService().taskService().removeActorByTaskId(taskId);
                //更新履历
                updateActiveHistory(instance,oldUser,newUser);
                //添加新任务
                TaskInstance newTaskInstance = addNewTaskInstance(instance);
                //添加新审批人
                addNewTaskActor(newTaskInstance.getId(),newUser);
                //添加新的履历
                addNewActiveHistory(newTaskInstance,newUser);

                //添加返回结果
                this.result.addCount();
                this.result.getTaskInstances().add(instance);
            }
            this.result.setCode(1);
        }
    }


    /**
     * 添加新历史履历
     *
     * @param newInstance  新任务实例
     * @param user         新审批人
     */
    private  void  addNewActiveHistory(TaskInstance newInstance,DefaultUser user){
        ActiveHistory newHistory = new ActiveHistory()
                .setId(ObjectHelper.primaryKey())
                .setTaskId(newInstance.getId())
                .setTaskType(newInstance.getTaskType())
                .setTaskName(newInstance.getTaskName())
                .setTaskDisplayName(newInstance.getDisplayName())
                .setProcessId(newInstance.getProcessId())
                .setOrderId(newInstance.getOrderId())
                .setCreateTime(TimeHelper.getNow())
                .setApproveUserVariable(JsonHelper.toJson(user));

        ProcessDefinition process = engine().repositoryService().processService().getProcessById(newInstance.getProcessId());
        if(!ObjectHelper.isEmpty(process)){
            ProcessModel model = process.getProcessModel();

            if(!ObjectHelper.isEmpty(model)){
                newHistory.setProcessName(model.getName())
                        .setProcessDisplayName(model.getDisplayName());
            }
        }

        engine().historyService().activeHistoryService().insert(newHistory);

    }


    /**
     * 添加新的审批人
     * @param newTaskId   新的任务ID
     * @param newUser     新的审批人
     */
    private void  addNewTaskActor(String newTaskId,DefaultUser newUser){
        engine().runtimeService().taskService().addActor(newTaskId,newUser);
    }

    /**
     * 添加新任务
     *
     * @param oldInstance  旧任务实例
     * @return             新任务实例
     */
    private  TaskInstance  addNewTaskInstance(TaskInstance oldInstance){
        TaskInstance instance = new TaskInstance()
                .setId(ObjectHelper.primaryKey())
                .setTaskName(oldInstance.getTaskName())
                .setDisplayName(oldInstance.getDisplayName())
                .setTaskType(oldInstance.getTaskType())
                .setApproveUser(oldInstance.getApproveUser())
                .setCreateTime(TimeHelper.getNow())
                .setApproveCount(oldInstance.getApproveCount())
                .setStatus(oldInstance.getStatus())
                .setOrderId(oldInstance.getOrderId())
                .setProcessId(oldInstance.getProcessId())
                .setVariable(oldInstance.getVariable())
                .setParentId(oldInstance.getParentId())
                .setVersion(oldInstance.getVersion())
                .setPosition(oldInstance.getPosition());

        engine().runtimeService().taskService().createNewTask(instance);
        return instance;
    }


    /**
     * 更新履历操作
     *
     * @param instance   任务实例
     * @param oldUser    旧审批人
     * @param newUser    新审批人
     */
    private void updateActiveHistory(TaskInstance instance,DefaultUser oldUser,DefaultUser newUser){
        QueryFilter filter = new QueryFilter()
                .setOrderId(instance.getOrderId())
                .setTaskId(instance.getId());

        List<ActiveHistory> histories = engine().historyService().activeHistoryService().queryList(filter);
        if(!ObjectHelper.isEmpty(histories)){
            ActiveHistory history = histories.get(0);
            updateActiveHistory(history,oldUser.userId(),newUser.userId());
        }
    }


    private void updateActiveHistory(ActiveHistory history,String oldUserId,String newUserId){
        history.setOperate(WorkflowOperate.transfer.value())
                .setSuggest("转办成功！"+oldUserId+"---->"+newUserId)
                .setApproveTime(TimeHelper.getNow())
                .setActorId(user().userId())
                .setActorName(user().userName())
                .setSubmitUserVariable(JsonHelper.toJson(user()));

        engine().historyService().activeHistoryService().update(history);
    }


    private void transfer(DefaultUser oldUser,DefaultUser newUser){
        //查询所有任务
        List<TaskActor> actors = engine().runtimeService().taskService().getTaskActorByActorId(oldUser.userId());

        if(!ObjectHelper.isEmpty(actors)){
           List<String> taskIds = actors.stream().map(TaskActor::getTaskId).collect(Collectors.toList());
           //执行转办
           transferOperate(taskIds,oldUser,newUser);
        }
    }


    private  void transferBySystem(String system,DefaultUser oldUser,DefaultUser newUser){
        //查询系统下所有流程实例
        QueryFilter orderFilter = new QueryFilter()
                .setSystem(system);
        transferByQueryFilter(orderFilter,oldUser,newUser);
    }


    private void transferByProcessId(String processId,DefaultUser oldUser ,DefaultUser newUser){
        QueryFilter orderFilter = new QueryFilter()
                .setProcessId(processId);
        transferByQueryFilter(orderFilter,oldUser,newUser);
    }


    private void transferByBusinessId(List<String> businessIds,DefaultUser oldUser,DefaultUser newUser){
        QueryFilter businessFilter = new QueryFilter()
                .setBusinessId(businessIds);
        List<OrderBusiness> orderBusinesses  = engine().runtimeService().orderService().queryOrderBusiness(businessFilter);

        if(!ObjectHelper.isEmpty(orderBusinesses)){
            List<String> orderIds = orderBusinesses.stream()
                    .map(OrderBusiness::getOrderId).collect(Collectors.toList());
            QueryFilter orderFilter = new QueryFilter()
                    .setOrderIds(orderIds.toArray(new String[]{}));
            transferByQueryFilter(orderFilter,oldUser,newUser);
        }
    }


    private void transferByOrderId(String orderId,DefaultUser oldUser,DefaultUser newUser){
        QueryFilter taskFilter = new QueryFilter()
                .setOrderId(orderId)
                .setTaskActorId(new String[]{oldUser.userId()});
        List<TaskInstance> taskInstances = engine().runtimeService().taskService().queryList(taskFilter);

        List<String> taskIds = new LinkedList<>();
        //任务ID集合
        if(!ObjectHelper.isEmpty(taskInstances)) {
            taskIds.addAll(taskInstances.stream().map(TaskInstance::getId).collect(Collectors.toList()));
            transferOperate(taskIds,oldUser,newUser);
        }
    }


    /**
     * 通过任务ID进行转办
     *
     * @param taskId   任务ID
     * @param oldUser  旧审批人
     * @param newUser  新审批人
     */
    private void transferByTaskId(String taskId,DefaultUser oldUser,DefaultUser newUser){
        QueryFilter taskFilter = new QueryFilter()
                .setTaskId(taskId)
                .setTaskActorId(new String[]{oldUser.userId()});
        List<TaskInstance> taskInstances = engine().runtimeService().taskService().queryList(taskFilter);

        if(!ObjectHelper.isEmpty(taskInstances)) {
            List<String> taskIds = new LinkedList<>();
            taskIds.addAll(taskInstances.stream().map(TaskInstance::getId).collect(Collectors.toList()));
            transferOperate(taskIds,oldUser,newUser);
        }
    }


    private void transferByQueryFilter(QueryFilter orderFilter,DefaultUser oldUser,DefaultUser newUser){
        List<OrderInstance> instances = engine().runtimeService().orderService().queryList(orderFilter);

        //查询流程实例下的任务实例
        List<String> taskIds = new LinkedList<>();
        if(!ObjectHelper.isEmpty(instances)){
            List<String> orderIds = instances.stream().map(OrderInstance::getId).collect(Collectors.toList());

            QueryFilter taskFilter = new QueryFilter()
                    .setOrderIds(orderIds.toArray(new String[]{}))
                    .setTaskActorId(new String[]{oldUser.userId()});
            List<TaskInstance> taskInstances = engine().runtimeService().taskService().queryList(taskFilter);

            //任务ID集合
            if(!ObjectHelper.isEmpty(taskInstances)) {
                taskIds.addAll(taskInstances.stream().map(TaskInstance::getId).collect(Collectors.toList()));
                transferOperate(taskIds,oldUser,newUser);
            }
        }
    }



    public TransferVariable getVariable() {
        return variable;
    }

    public TransferManager setVariable(TransferVariable variable) {
        this.variable = variable;
        return this;
    }


    protected DefaultUser user(){
        DefaultUser user = variable.getUser();
        if(ObjectHelper.isEmpty(user)){
            user = new DefaultUser().setUserId(WIND_ADMIN).setUserName("工作流管理员");
        }
        return user;
    }
}
