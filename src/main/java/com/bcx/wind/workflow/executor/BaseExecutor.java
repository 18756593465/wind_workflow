package com.bcx.wind.workflow.executor;

import com.bcx.wind.workflow.WorkflowEngine;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.Actuator;
import com.bcx.wind.workflow.core.constant.WorkflowOperateConstant;
import com.bcx.wind.workflow.core.flow.*;
import com.bcx.wind.workflow.core.handler.*;
import com.bcx.wind.workflow.core.pojo.*;
import com.bcx.wind.workflow.entity.ActiveHistory;
import com.bcx.wind.workflow.entity.OrderBusiness;
import com.bcx.wind.workflow.entity.OrderInstance;
import com.bcx.wind.workflow.entity.TaskInstance;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.message.MessageHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bcx.wind.workflow.core.constant.NodeName.PROCESS;
import static com.bcx.wind.workflow.core.constant.NodeName.START;
import static com.bcx.wind.workflow.message.MsgConstant.*;

/**
 * @author zhanglei
 */
public abstract class BaseExecutor implements Executor{


    /**
     * 执行数据
     */
    protected Actuator actuator;

    public BaseExecutor(Actuator actuator){
        this.actuator = actuator;
    }

    public Actuator getActuator() {
        return actuator;
    }

    public void setActuator(Actuator actuator) {
        this.actuator = actuator;
    }

    public abstract void  executor();

    protected WorkflowEngine engine(){
        return actuator.getEngine();
    }

    protected WorkflowVariable variable(){
        return this.actuator.getVariable();
    }


    /**
     * 创建工作流  主要操作  创建流程实例   创建执行履历   创建第一条任务   为wind_active_history
     *  wind_business_id   wind_order_instance    wind_task_instance  添加数据
     * @param variable   创建参数  processId or processName ,  businessId , user ， dataMap
     * @return   workflow
     */
    @Override
    public Workflow buildWorkflow(WorkflowVariable variable) {
        addWorkflow(variable);
        this.executor();
        return this.actuator.getWorkflow();
    }

    @Override
    public <T extends BuildHandler> Workflow buildWorkflow(WorkflowVariable variable, T handler) {
        handler.beforeBuildHandler(actuator.getWorkflow());
        buildWorkflow(variable);
        handler.afterBuildHandler(actuator.getWorkflow());
        return this.actuator.getWorkflow();
    }


    @Override
    public Workflow submit(WorkflowVariable variable) {
        addWorkflow(variable);
        this.executor();
        return this.actuator.getWorkflow();
    }


    @Override
    public <T extends SubmitHandler> Workflow submit(WorkflowVariable variable, T handler) {
        handler.beforeSubmitHandler(this.actuator.getWorkflow());
        submit(variable);
        handler.afterSubmitHandler(this.actuator.getWorkflow());
        return this.actuator.getWorkflow();
    }


    @Override
    public Workflow reject(WorkflowVariable variable) {
        this.executor();
        return this.actuator.getWorkflow();
    }


    @Override
    public <T extends RejectHandler> Workflow reject(WorkflowVariable variable, T handler) {
        handler.beforeRejectHandler(this.actuator.getWorkflow());
        reject(variable);
        handler.afterRejectHandler(this.actuator.getWorkflow());
        return this.actuator.getWorkflow();
    }


    @Override
    public Workflow withDraw(WorkflowVariable variable) {
        addWorkflow(variable);
        this.executor();
        return this.actuator.getWorkflow();
    }


    @Override
    public <T extends WithdrawHandler> Workflow withDraw(WorkflowVariable variable, T handler) {
        handler.beforeWithdrawHandler(this.actuator.getWorkflow());
        withDraw(variable);
        handler.afterWithdrawHandler(this.actuator.getWorkflow());
        return this.actuator.getWorkflow();
    }


    @Override
    public Workflow complete(WorkflowVariable variable) {
        addWorkflow(variable);
        this.executor();
        return this.actuator.getWorkflow();
    }


    @Override
    public <T extends CompleteHandler> Workflow complete(WorkflowVariable variable, T handler) {
        handler.beforeCompleteHandler(this.actuator.getWorkflow());
        complete(variable);
        handler.afterCompleteHandler(this.actuator.getWorkflow());
        return this.actuator.getWorkflow();
    }



    private void addWorkflow(WorkflowVariable variable){
        workflow().setSystem(variable.getSystem())
                .setOrderId(variable.getOrderId())
                .setBusinessId(variable.getBusinessId())
                .setApproveUsers(variable.getApproveUsers())
                .setVariable(variable)
                .setUser(variable.getUser())
                .setDataMap(variable.getDataMap());
    }


    /**
     * 创建当前任务
     * @param curNode  当前任务
     */
    protected void createCurTask(TaskModel curNode, TaskInstance taskInstance){
        String processId = actuator.getProcessModel().getProcessId();
        //自定义提交节点
        List<NodeModel> taskNodes = nextTasks(curNode);

        List<Task> nextTasks = new LinkedList<>();
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
                .setNextTask(nextTasks)
                .setTaskInstance(taskInstance);

        workflow().getCurTask().add(curTask);
    }


    private List<NodeModel> nextTasks(TaskModel curNode){
        List<NodeModel> taskNodes = new LinkedList<>();
        String submitNode = variable().getSubmitNode();
        //如果是指定提交节点
        if(!ObjectHelper.isEmpty(submitNode)) {
            NodeModel nodeModel = this.actuator.getProcessModel().getTaskModel(submitNode);
            Assert.notEmpty(MessageHelper.getMsg(w019), nodeModel);

            Assert.isTrue(MessageHelper.getMsg(w020), nodeModel instanceof ScribeTaskNode || nodeModel instanceof CustomNode);
            TaskNode node = (TaskNode) nodeModel;
            Assert.isTrue(MessageHelper.getMsg(w020), node.isInAnd() || node.isInOr());
            taskNodes.add(node);
            return taskNodes;
        }

        taskNodes = curNode.nextTaskNodes();
        List<NodeModel> nextNodes = curNode.nextNodes();
        if(!ObjectHelper.isEmpty(nextNodes)) {
            NodeModel nextNode = nextNodes.get(0);
            if (nextNode instanceof RouterNode) {
                RouterNode routerNode = (RouterNode) nextNodes.get(0);
                taskNodes.clear();
                taskNodes.add(routerNode.getFirstTaskNode());
            }else if(nextNode.parentNode() instanceof RouterNode && nextNode instanceof EndNode){
                taskNodes.clear();
                RouterNode routerNode = (RouterNode) nextNode.parentNode();
                taskNodes.clear();
                taskNodes.addAll(routerNode.nextTaskNodes());
            }
        }
        return taskNodes;
    }


    protected  void  buildCurTask(){
        //当前用户
        User user = variable().getUser();

        //流程实例号
        String orderId = workflow().getOrderId();
        if(ObjectHelper.isEmpty(orderId)) {
            if (workflow().getChildOrderInstance().isEmpty()) {
                orderId = variable().getOrderId();
            } else {
                orderId = workflow().getChildOrderInstance().get(0).getId();
            }
            workflow().setOrderId(orderId);
        }

        //流程下的用户集合
        QueryFilter filter = new QueryFilter()
                .setOrderId(orderId);
        List<TaskInstance> taskInstances = engine().runtimeService().taskService().queryList(filter);
        Assert.notEmpty(MessageHelper.getMsg(w015,orderId),taskInstances);

        //当前节点名称
        String taskName = taskInstances.get(0).getTaskName();
        TaskModel taskNode = this.actuator.getProcessModel().getTaskModel(taskName);

        //是开始任务
        if(START.equals(taskNode.lastNodes().get(0).nodeType().value()) && PROCESS.equals(taskNode.parentNode().nodeType().value())){
            List<ActiveHistory> histories = engine().historyService().activeHistoryService().queryList(new QueryFilter().setOrderId(orderId));
            if(histories.size()<=1){
                createCurTask(taskNode,taskInstances.get(0));
            }else{
                filter.setTaskActorId(new String[]{user.userId()});
                taskInstances = engine().runtimeService().taskService().queryList(filter);
                Assert.notEmpty(MessageHelper.getMsg(w016,user.userName(),orderId),taskInstances);
                for(TaskInstance taskInstance : taskInstances){
                    String name = taskInstance.getTaskName();
                    TaskModel node = this.actuator.getProcessModel().getTaskModel(name);
                    createCurTask(node,taskInstance);
                }
            }
        }else{
            filter.setTaskActorId(new String[]{user.userId()});
            taskInstances = engine().runtimeService().taskService().queryList(filter);
            Assert.notEmpty(MessageHelper.getMsg(w016,user.userName(),orderId),taskInstances);
            for(TaskInstance taskInstance : taskInstances){
                String name = taskInstance.getTaskName();
                TaskModel node = this.actuator.getProcessModel().getTaskModel(name);
                createCurTask(node,taskInstance);
            }
        }

    }


    protected Workflow  workflow(){
        return this.actuator.getWorkflow();
    }


    /**
     * 查询创建流程实例数据，包含子流程实例数据
     *
     * 查询子流程实例
     */
    protected   void  buildOrderInstance(){
        String orderId = variable().getOrderId();
        //流程实例
        OrderInstance orderInstance = engine().runtimeService().orderService().queryOne(orderId);
        Assert.notEmpty(MessageHelper.getMsg(w014,orderId),orderInstance);

        String parentOrderId = orderInstance.getParentId();
        int i=0;
        while(i<5) {
            if (!ObjectHelper.isEmpty(parentOrderId)) {
                orderInstance = engine().runtimeService().orderService().queryOne(parentOrderId);
                Assert.notEmpty(MessageHelper.getMsg(w014,orderId),orderInstance);
                parentOrderId = orderInstance.getParentId();
                if(ObjectHelper.isEmpty(parentOrderId)){
                    break;
                }
                workflow().getChildOrderInstance().add(orderInstance);
            }else{
                workflow().setOrderInstance(orderInstance);
                break;
            }
            i++;
        }

        QueryFilter filter = new QueryFilter()
                .setOrderId(orderId);
        List<OrderBusiness> businesses = engine().runtimeService().orderService().queryOrderBusiness(filter);
        if(!ObjectHelper.isEmpty(businesses)){
            workflow().setBusinessId(businesses.stream().map(OrderBusiness::getBusinessId).collect(Collectors.toList()));
        }

    }


    protected void buildWorkflow(){
        //查询流程实例
        if(!WorkflowOperateConstant.REJECT.equals(this.actuator.getOperate().name())) {
            buildOrderInstance();

            this.actuator.getWorkflow().setVariable(variable()).setApproveUsers(variable().getApproveUsers())
                    .setSystem(variable().getSystem())
                    .setBusinessId(variable().getBusinessId())
                    .setUser(variable().getUser());

            //当前任务
            buildCurTask();
        }
    }

    protected DefaultUser user(){
        return workflow().getUser();
    }

}
