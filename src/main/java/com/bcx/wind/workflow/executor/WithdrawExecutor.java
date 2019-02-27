package com.bcx.wind.workflow.executor;

import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.Actuator;
import com.bcx.wind.workflow.core.constant.TaskStatus;
import com.bcx.wind.workflow.core.pojo.Task;
import com.bcx.wind.workflow.entity.ActiveHistory;
import com.bcx.wind.workflow.entity.TaskInstance;
import com.bcx.wind.workflow.helper.JsonHelper;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.TimeHelper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 撤回执行，将流程以及相关业务数据撤回到最原始的尚未提交的状态，可以重新创建工作流，保留历史
 *
 * @author zhanglei
 */
public class WithdrawExecutor extends BaseExecutor {


    public WithdrawExecutor(Actuator actuator) {
        super(actuator);
    }


    @Override
    public void executor() {
        //当前流程实例
        buildMainOrder();
        //构建当前任务
        buildWorkflow();
        //执行撤回
        withDraw();
    }


    private  void  withDraw(){
        //删除流程实例
        engine().runtimeService().orderService().deleteById(workflow().getOrderId());
        engine().runtimeService().orderService().deleteByParentId(workflow().getOrderId());

        //任务以及审批人
        QueryFilter filter = new QueryFilter()
                .setOrderId(workflow().getOrderId())
                .setStatus(TaskStatus.RUN);
        List<TaskInstance> taskInstances = engine().runtimeService().taskService().queryList(filter);
        if(!ObjectHelper.isEmpty(taskInstances)){
            List<String> ids = taskInstances.stream().map(TaskInstance::getId).collect(Collectors.toList());
            //删除审批人
            engine().runtimeService().taskService().removeActorByTaskIds(ids);

            //删除任务
            engine().runtimeService().taskService().removeByTaskIds(ids);
        }

        //更新历史
        updateHistory();
    }



    private void updateHistory(){
        List<Task> tasks = workflow().getCurTask();
        if(!ObjectHelper.isEmpty(tasks)){

            for(Task task : tasks){
                String taskId = task.getTaskInstance().getId();
                QueryFilter filter = new QueryFilter()
                        .setOrderId(workflow().getOrderId())
                        .setTaskId(taskId);
                List<ActiveHistory> activeHistories = engine().historyService().activeHistoryService().queryList(filter);

                if(!ObjectHelper.isEmpty(activeHistories)){
                    ActiveHistory history = activeHistories.get(0);
                    history.setOperate(this.actuator.getOperate().value())
                            .setSuggest(variable().getSuggest())
                            .setApproveTime(TimeHelper.getNow())
                            .setActorId(user().userId())
                            .setActorName(user().userName())
                            .setSubmitUserVariable(JsonHelper.toJson(user()));

                    //更新
                    engine().historyService().activeHistoryService().update(history);
                }
            }
        }
    }

}
