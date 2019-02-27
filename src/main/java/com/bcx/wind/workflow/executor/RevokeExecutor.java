package com.bcx.wind.workflow.executor;

import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.Actuator;
import com.bcx.wind.workflow.core.flow.*;
import com.bcx.wind.workflow.core.pojo.ApproveUser;
import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.entity.ActiveHistory;
import com.bcx.wind.workflow.entity.OrderBusiness;
import com.bcx.wind.workflow.entity.OrderInstance;
import com.bcx.wind.workflow.entity.TaskInstance;
import com.bcx.wind.workflow.exception.WorkflowException;
import com.bcx.wind.workflow.executor.handler.TaskHandler;
import com.bcx.wind.workflow.helper.JsonHelper;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.TimeHelper;
import com.bcx.wind.workflow.message.MessageHelper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bcx.wind.workflow.core.constant.OrderVariableKey.*;
import static com.bcx.wind.workflow.message.MsgConstant.w026;

/**
 * 撤销操作
 *
 * @author zhanglei
 */
public class RevokeExecutor extends BaseExecutor {



    public RevokeExecutor(Actuator actuator) {
        super(actuator);
    }


    @Override
    public void executor() {
        //构建流程实例
        buildMainOrder();

        //执行撤销
        revoke();
    }


    @SuppressWarnings("unchecked")
    private  void  revoke(){
        //获取撤销信息
        OrderInstance instance = workflow().getOrderInstance();
        Object revokeVar = instance.getVariableMap().get(REVOKE_VARIABLE);
        //不为空
        if(!ObjectHelper.isEmpty(revokeVar)){
            Map<String,Object> args = JsonHelper.coverObject(revokeVar,Map.class,String.class,Object.class);
            Object taskName = args.get(REVOKE_NODE);

            if(!ObjectHelper.isEmpty(taskName)){
                Object  taskIdObj = args.get(NEW_TASK_ID);

                if(!ObjectHelper.isEmpty(taskIdObj)){
                    List<String> taskIds = JsonHelper.coverObject(taskIdObj,List.class,String.class);
                    QueryFilter filter = new QueryFilter()
                            .setOrderId(instance.getId());
                    List<TaskInstance> taskInstances = engine().runtimeService().taskService().queryList(filter);

                    for(String id : taskIds){
                        List<TaskInstance> taskInstance = taskInstances.stream().filter(task->task.getId().equals(id))
                                .collect(Collectors.toList());

                        if(!ObjectHelper.isEmpty(taskInstance)){
                            revokeOperate(taskName.toString(),taskInstances);
                            break;
                        }
                    }

                }
            }
        }
    }


    @SuppressWarnings("unchecked")
    private  void  revokeOperate(String revokeNode,List<TaskInstance> taskInstances){
        //删除当前流程实例下所有任务,及审批人
        engine().runtimeService().taskService().removeByOrderId(workflow().getOrderId());
        engine().runtimeService().taskService().removeActorByTaskIds(taskInstances.stream().map(TaskInstance::getId).collect(Collectors.toList()));
        //更新撤销操作相关数据
        updateRevokeMsg(taskInstances);


        //撤销到指定节点
        //查询撤销节点的审批人
        Object taskApproveUser = workflow().getOrderInstance().getVariableMap().get(TASK_APPROVE_USER);
        if(!ObjectHelper.isEmpty(taskApproveUser)){
            Map<String,List<DefaultUser>>  approveUsers = JsonHelper.coverObject(taskApproveUser,Map.class,String.class,List.class);
            List<DefaultUser> users = approveUsers.get(revokeNode);
            users = JsonHelper.coverObject(users,List.class,DefaultUser.class);
            if(!ObjectHelper.isEmpty(users)){
                //设置审批人
                workflow().setApproveUsers(Collections
                        .singletonList(new ApproveUser().setNodeId(revokeNode).setApproveUsers(users)));

                NodeModel nodeModel = processModel().getTaskModel(revokeNode);
                //回滚重新创建
                new TaskHandler(this.actuator,nodeModel,null).handler();
            }
        }
    }


    private  void  updateRevokeMsg(List<TaskInstance> nowTasks){
        if(!ObjectHelper.isEmpty(nowTasks)){
            for(TaskInstance taskInstance : nowTasks){
                //更新历史
                QueryFilter filter = new QueryFilter()
                        .setOrderId(workflow().getOrderId())
                        .setTaskId(taskInstance.getId());
                List<ActiveHistory> histories = engine().historyService().activeHistoryService().queryList(filter);
                if(!ObjectHelper.isEmpty(histories)){
                    ActiveHistory history = histories.get(0);
                    history.setOperate(this.actuator.getOperate().value())
                            .setSuggest(variable().getSuggest())
                            .setApproveTime(TimeHelper.getNow())
                            .setActorId(user().userId())
                            .setActorName(user().userName());
                    engine().historyService().activeHistoryService().update(history);
                }


                //如果正在执行的任务在子流程中的第一个节点，则删除子流程实例数据
                String name = taskInstance.getTaskName();
                TaskModel model = processModel().getTaskModel(name);
                if(!ObjectHelper.isEmpty(model) && model instanceof TaskNode
                        && ((TaskNode) model).inRouter() && model.lastNodes().get(0) instanceof StartNode){
                    //删除子流程实例
                    engine().runtimeService().orderService().deleteByParentId(workflow().getOrderId());
                }
            }
        }
    }

    private ProcessModel processModel(){
        return this.actuator.getProcessModel();
    }



}
