package com.bcx.wind.workflow.executor;

import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.Actuator;
import com.bcx.wind.workflow.core.constant.OrderType;
import com.bcx.wind.workflow.core.flow.*;
import com.bcx.wind.workflow.core.lock.BusinessLock;
import com.bcx.wind.workflow.core.lock.Content;
import com.bcx.wind.workflow.core.pojo.Task;
import com.bcx.wind.workflow.core.pojo.WorkflowVariable;
import com.bcx.wind.workflow.entity.OrderBusiness;
import com.bcx.wind.workflow.entity.OrderInstance;
import com.bcx.wind.workflow.helper.JsonHelper;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.TimeHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * 启动创建执行
 *
 * @author zhanglei
 */
public class BuildExecutor extends BaseExecutor {

    /**
     * 是否为原始创建
     */
    private boolean isBuild = true;

    public BuildExecutor(Actuator actuator) {
        super(actuator);
    }

    @Override
    public void executor() {
        //校验重复操作
        checkSameOperate(variable());

        //构建流程实例
        createMainOrder(variable());

        //构建当前任务
        if(!this.isBuild) {
            buildCurTask();
        }else{
            buildFirstTask();
            List<Task> tasks = workflow().getCurTask();
            for(Task task : tasks) {
                StartNode startNode = (StartNode) this.actuator.getProcessModel().getNodeModel();
                startNode.actuator(this.actuator);
                startNode.task(task);
                startNode.executor();
            }
        }
    }

    private void buildFirstTask(){
        TaskNode curNode = (TaskNode) actuator.getProcessModel().getFirstTaskNode();
        String processId = actuator.getProcessModel().getProcessId();
        List<NodeModel> taskNodes = curNode.nextTaskNodes();
        List<NodeModel> nextNodes = curNode.nextNodes();
        List<Task> nextTasks = new LinkedList<>();

        if(nextNodes.get(0) instanceof RouterNode){
            RouterNode routerNode = (RouterNode)nextNodes.get(0);
            taskNodes.clear();
            taskNodes.add(routerNode.getFirstTaskNode());
        }
        for(NodeModel next : taskNodes){
            Task task = new Task()
                    .setProcessId(processId)
                    .setTaskName(next.name())
                    .setTaskModel((TaskModel) next)
                    .setTaskConfig(ObjectHelper.getTaskConfig(this.actuator.getConfiguration(), (TaskModel) next,this.actuator.getDataMap()));

            nextTasks.add(task);
        }

        Task curTask = new Task()
                .setProcessId(processId)
                .setTaskName(curNode.name())
                .setTaskModel(curNode)
                .setTaskConfig(ObjectHelper.getTaskConfig(this.actuator.getConfiguration(), curNode,this.actuator.getDataMap()))
                .setNextTask(nextTasks);

        workflow().getCurTask().add(curTask);
    }

    /**
     * 校验重复的业务操作
     *
     * @param variable  参数
     */
    private void checkSameOperate(WorkflowVariable variable){
        //数据并发重复
        List<String> businessIds = variable.getBusinessId();
        BusinessLock.execute(new Content(businessIds));
    }



    /**
     * 创建主流程实例
     *
     * @param variable  参数
     */
    private void createMainOrder(WorkflowVariable variable){
        ProcessModel model = actuator.getProcessModel();

        //已经存在
        List<String> businessIds = variable.getBusinessId();
        QueryFilter filter = new QueryFilter()
                .setBusinessId(businessIds);
        if(!ObjectHelper.isEmpty(variable.getSystem())){
            filter.setSystem(variable.getSystem());
        }
        List<OrderBusiness> businesses = engine().runtimeService().orderService().
                queryOrderBusiness(filter);
        if(!ObjectHelper.isEmpty(businesses)){
            this.isBuild = false;
            OrderInstance orderInstance = engine().runtimeService().orderService().queryOne(businesses.get(0).getOrderId());
            this.actuator.setOrderInstance(orderInstance);
            this.actuator.getWorkflow().setOrderId(orderInstance.getId()).setOrderInstance(orderInstance);
            return;
        }

        //新建
        OrderInstance orderInstance = new OrderInstance()
                .setId(ObjectHelper.primaryKey())
                .setData(JsonHelper.toJson(variable.getDataMap()))
                .setProcessId(model.getProcessId())
                .setStatus(OrderType.RUN)
                .setCreateUser(variable.getUser().userId())
                .setCreateTime(TimeHelper.getNow())
                .setVersion(1)
                .setSystem(variable.getSystem());

        engine().runtimeService().orderService().insert(orderInstance);

        this.actuator.setOrderInstance(orderInstance);

        this.actuator.getWorkflow().setOrderId(orderInstance.getId())
                .setOrderInstance(orderInstance);

        //新增业务id关联流程实例
        engine().runtimeService().orderService()
                .insertOrderBusiness(businessIds,orderInstance.getId(),variable.getSystem());
    }


}
