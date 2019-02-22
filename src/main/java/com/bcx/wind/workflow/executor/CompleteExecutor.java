package com.bcx.wind.workflow.executor;

import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.Actuator;
import com.bcx.wind.workflow.core.constant.OrderType;
import com.bcx.wind.workflow.core.pojo.Task;
import com.bcx.wind.workflow.entity.ActiveHistory;
import com.bcx.wind.workflow.entity.OrderHistoryInstance;
import com.bcx.wind.workflow.entity.OrderInstance;
import com.bcx.wind.workflow.entity.TaskInstance;
import com.bcx.wind.workflow.helper.JsonHelper;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.TimeHelper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 完结执行
 */
public class CompleteExecutor extends BaseExecutor {


    public CompleteExecutor(Actuator actuator) {
        super(actuator);
    }

    @Override
    public void executor() {
        buildWorkflow();

        //删除任务实例 审批人
        removeTaskInstance();

        //删除流程实例  添加历史流程实例
        removeOrderInstance();

        //更新执行履历
        updateHistory();

        //删除执行履历  添加完毕履历
        addCompleteHistory();
    }


    private void  addCompleteHistory(){
        List<ActiveHistory> activeHistories = engine().historyService().activeHistoryService()
                .queryList(new QueryFilter().setOrderId(workflow().getOrderId()));
        engine().historyService().completeHistoryService().insertList(activeHistories);

        //删除执行履历
        List<String> ids = activeHistories.stream().map(ActiveHistory::getId).collect(Collectors.toList());
        engine().historyService().activeHistoryService().deleteByIds(ids);
    }



    private  void  updateHistory(){
        //当前任务
        List<Task> curTasks = workflow().getCurTask();
        for(Task task : curTasks){
            String taskId = task.getTaskInstance().getId();
            //执行履历
            List<ActiveHistory> historys = engine().historyService().activeHistoryService()
                     .queryList(new QueryFilter().setTaskId(taskId));

            if(!ObjectHelper.isEmpty(historys)){
                ActiveHistory history = historys.get(0)
                        .setOperate(this.actuator.getOperate().value())
                        .setSuggest(variable().getSuggest())
                        .setApproveTime(TimeHelper.getNow())
                        .setActorId(user().userId())
                        .setActorName(user().userName())
                        .setSubmitUserVariable(JsonHelper.toJson(user()));

                engine().historyService().activeHistoryService().update(history);
            }
        }

    }


    private void removeTaskInstance(){
        QueryFilter filter = new QueryFilter()
                .setOrderId(workflow().getOrderId());

        //任务实例集合
        List<TaskInstance> taskInstanceList = engine().runtimeService().taskService().queryList(filter);
        //删除任务实例
        engine().runtimeService().taskService().removeByOrderId(workflow().getOrderId());
        //任务id集合
        List<String> taskIds = taskInstanceList.stream().map(TaskInstance::getId).collect(Collectors.toList());
        //删除审批人
        engine().runtimeService().taskService().removeActorByTaskIds(taskIds);
    }



    private void removeOrderInstance(){
        //流程实例
        OrderInstance instance = engine().runtimeService().orderService().queryOne(workflow().getOrderId());
        if(!ObjectHelper.isEmpty(instance)){
            //添加历史流程实例
            OrderHistoryInstance historyInstance = buildOrderHistory(instance);
            engine().historyService().orderHistoryService().insert(historyInstance);
        }
        //删除流程实例
        engine().runtimeService().orderService().deleteById(workflow().getOrderId());
    }


    private OrderHistoryInstance buildOrderHistory(OrderInstance instance){
         return new OrderHistoryInstance()
                 .setId(instance.getId())
                 .setProcessId(instance.getId())
                 .setStatus(OrderType.COMPLETE)
                 .setCreateUser(instance.getCreateUser())
                 .setCreateTime(instance.getCreateTime())
                 .setExpireTime(instance.getExpireTime())
                 .setParentId(instance.getParentId())
                 .setVersion(instance.getVersion())
                 .setVariable(instance.getVariable())
                 .setData(instance.getData())
                 .setSystem(instance.getSystem());
    }

}
