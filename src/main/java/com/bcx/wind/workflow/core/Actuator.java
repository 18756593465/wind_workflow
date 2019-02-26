package com.bcx.wind.workflow.core;

import com.bcx.wind.workflow.WorkflowEngine;
import com.bcx.wind.workflow.core.constant.WorkflowOperate;
import com.bcx.wind.workflow.core.constant.WorkflowOperateConstant;
import com.bcx.wind.workflow.core.flow.ProcessModel;
import com.bcx.wind.workflow.core.pojo.Configuration;
import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.core.pojo.Workflow;
import com.bcx.wind.workflow.core.pojo.WorkflowVariable;
import com.bcx.wind.workflow.entity.OrderInstance;
import com.bcx.wind.workflow.entity.TaskInstance;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 工作流执行配置数据集
 *
 * @author zhanglei
 */
public class Actuator {

    /**
     * 执行任务点，当执行提交时，任务走到下一个节点时，停止递归子节点
     */
    private volatile  int  taskPoint = 0;

    /**
     * 流程模型
     */
    private ProcessModel processModel;

    /**
     * 执行引擎
     */
    private WorkflowEngine engine;

    /**
     * 流程配置相关数据
     */
    private Configuration configuration;


    /**
     * 业务相关数据
     */
    private Map<String,Object> dataMap = new HashMap<>();

    /**
     * 流程实例
     */
    private OrderInstance orderInstance;

    /**
     * 当前会签任务集合
     */
    private List<TaskInstance> taskInstance = new LinkedList<>();


    /**
     * 当前会签任务的审批人
     */
    private List<DefaultUser> actors = new LinkedList<>();

    /**
     * 当前流程中正在执行的所有任务集合
     */
    private List<TaskInstance> nowAllTasks = new LinkedList<>();

    /**
     * 工作流执行实例  提供给业务系统
     */
    private Workflow   workflow;


    /**
     * 工作流审批参数
     */
    private WorkflowVariable variable;

    /**
     * 操作
     */
    private WorkflowOperate operate;


    public ProcessModel getProcessModel() {
        return processModel;
    }

    public Actuator setProcessModel(ProcessModel processModel) {
        this.processModel = processModel;
        return this;
    }

    public WorkflowEngine getEngine() {
        return engine;
    }

    public Actuator setEngine(WorkflowEngine engine) {
        this.engine = engine;
        return this;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Actuator setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public Actuator setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
        return this;
    }

    public OrderInstance getOrderInstance() {
        return orderInstance;
    }

    public Actuator setOrderInstance(OrderInstance orderInstance) {
        this.orderInstance = orderInstance;
        return this;
    }

    public List<TaskInstance> getTaskInstance() {
        return taskInstance;
    }

    public Actuator setTaskInstance(List<TaskInstance> taskInstance) {
        this.taskInstance = taskInstance;
        return this;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public Actuator setWorkflow(Workflow workflow) {
        this.workflow = workflow;
        return this;
    }

    public int getTaskPoint() {
        return taskPoint;
    }

    public Actuator setTaskPoint(int taskPoint) {
        this.taskPoint = taskPoint;
        return this;
    }


    public WorkflowVariable getVariable() {
        return variable;
    }

    public Actuator setVariable(WorkflowVariable variable) {
        this.variable = variable;
        return this;
    }

    public WorkflowOperate getOperate() {
        return operate;
    }

    public Actuator setOperate(WorkflowOperate operate) {
        this.operate = operate;
        return this;
    }

    public List<DefaultUser> getActors() {
        return actors;
    }

    public Actuator setActors(List<DefaultUser> actors) {
        this.actors = actors;
        return this;
    }

    public List<TaskInstance> getNowAllTasks() {
        return nowAllTasks;
    }

    public Actuator setNowAllTasks(List<TaskInstance> nowAllTasks) {
        this.nowAllTasks = nowAllTasks;
        return this;
    }
}
