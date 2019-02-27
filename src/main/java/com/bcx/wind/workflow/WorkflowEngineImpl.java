package com.bcx.wind.workflow;

import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.Actuator;
import com.bcx.wind.workflow.core.WorkflowService;
import com.bcx.wind.workflow.core.WorkflowSession;
import com.bcx.wind.workflow.core.pojo.Configuration;
import com.bcx.wind.workflow.core.pojo.ManagerData;
import com.bcx.wind.workflow.core.pojo.NodeConfig;
import com.bcx.wind.workflow.core.pojo.Workflow;
import com.bcx.wind.workflow.entity.ProcessConfig;
import com.bcx.wind.workflow.entity.ProcessDefinition;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.impl.DefaultWorkflowSession;
import com.bcx.wind.workflow.impl.WorkflowServiceImpl;

import java.util.List;
import java.util.Map;

import static com.bcx.wind.workflow.core.constant.NodeConfigConstant.*;

/**
 * 工作流核心接口实现
 *
 * @author zhanglei
 */
public class WorkflowEngineImpl implements WorkflowEngine {

    private Context context;

    /**
     * 资源模块
     */
    private RepositoryService repositoryService;

    /**
     * 历史模块
     */
    private HistoryService historyService;

    /**
     * 运行时模块
     */
    private RuntimeService runtimeService;


    private WorkflowEngineImpl(){}


    public static WorkflowEngine build(RepositoryService repositoryService, HistoryService historyService, RuntimeService runtimeService){
        WorkflowEngineImpl engine = WorkflowEngineCreate.workflowEngine;
        engine.repositoryService = repositoryService;
        engine.historyService = historyService;
        engine.runtimeService = runtimeService;
        return engine;
    }



    private static class WorkflowEngineCreate{
        private WorkflowEngineCreate(){}
        private static WorkflowEngineImpl workflowEngine = new WorkflowEngineImpl();
    }

    @Override
    public RepositoryService repositoryService() {
        return this.repositoryService;
    }


    @Override
    public HistoryService historyService() {
        return this.historyService;
    }


    @Override
    public RuntimeService runtimeService() {
        return this.runtimeService;
    }

    @Override
    public WorkflowSession openWorkflowSession(String processName) {
        //流程模型
        ProcessDefinition definition = processDefinition(processName);

        //配置数据
        List<ProcessConfig> configList = getProcessConfigList(definition.getId());

        Configuration configuration = buildConfiguration(configList,processName);
        //构造执行器
        Actuator actuator = new Actuator().setConfiguration(configuration)
                .setProcessModel(definition.getProcessModel())
                .setEngine(this)
                .setWorkflow(new Workflow()
                        .setProcessDefinition(definition)
                        .setEngine(this)
                        .setProcessConfig(configuration));

        return  new DefaultWorkflowSession(actuator);
    }

    @Override
    public WorkflowService openWorkflowService() {
        ManagerData data = new ManagerData()
                .setEngine(this);
        return new WorkflowServiceImpl(data);
    }

    @Override
    public Context getContext() {
        return this.context;
    }

    @Override
    public void context(Context context) {
        this.context = context;
    }


    private Configuration buildConfiguration(List<ProcessConfig> configList, String processName){
        Configuration configuration = null;

        for(ProcessConfig config : configList){
            if(processName.equals(config.getNodeId()) && config.getLevel()==1){
                Map<String,Object> map = config.getNodeConfigMap();
                configuration = createConfiguration(config,map,configList);
                break;
            }
        }

        if(!ObjectHelper.isEmpty(configuration)) {

            for (ProcessConfig config : configList) {
                if (processName.equals(config.getNodeId()) && config.getLevel() == 1) {
                    continue;
                }

                Map<String, Object> map = config.getNodeConfigMap();
                Configuration con  = createConfiguration(config, map, configList);
                configuration.addConfigurationList(con);
            }
        }

        return configuration;
    }


    private Configuration createConfiguration(ProcessConfig config, Map<String,Object> map, List<ProcessConfig> configList){
        int jointlyApproveUserSet = ObjectHelper.isEmpty(map.get(JOINTLY_APPROVE_USER_SET)) ?
                0 : (int)map.get(JOINTLY_APPROVE_USER_SET);

        int transferTimeSet = ObjectHelper.isEmpty(map.get(TRANSFER_TIME_SET)) ?
                0 : (int)map.get(TRANSFER_TIME_SET);

        int jointlyApproveUserMinCount = ObjectHelper.isEmpty(map.get(JOINTLY_APPROVE_USER_MIN_COUNT)) ?
                0 : (int)map.get(JOINTLY_APPROVE_USER_MIN_COUNT);

        int emailSet = ObjectHelper.isEmpty(map.get(EMAIL_SET)) ?
                0 : (int)map.get(EMAIL_SET);

        int jointly = ObjectHelper.isEmpty(map.get(JOINTLY)) ? 0 : (int)map.get(JOINTLY);

        int freeFlowSet = ObjectHelper.isEmpty(map.get(FREE_FLOW_SET)) ? 0 : (int)map.get(FREE_FLOW_SET);

        NodeConfig nodeConfig = new NodeConfig()
                .setReturnNode(ObjectHelper.getValue(RETURN_NODE,map))
                .setSubmitLine(ObjectHelper.getValue(SUBMIT_LINE,map))
                .setTaskContent(ObjectHelper.getValue(TASK_CONTENT,map))
                .setTaskLink(ObjectHelper.getValue(TASK_LINK,map))
                .setJointlyApproveUserSet(jointlyApproveUserSet)
                .setTransferTimeSet(transferTimeSet)
                .setJointlyApproveTimeSet(ObjectHelper.getValue(JOINTLY_APPROVE_TIME_SET,map))
                .setJointlyApproveUserMinCount(jointlyApproveUserMinCount)
                .setEmailSet(emailSet)
                .setJointly(jointly)
                .setFreeFlowSet(freeFlowSet);


        return  new Configuration().setProcessId(config.getProcessId())
                .setApproveUser(config.getApproveUsers())
                .setConditionConfig(config.getConditions())
                .setConfigList(configList)
                .setConfigName(config.getConfigName())
                .setLogic(config.getLogic())
                .setNodeConfig(nodeConfig)
                .setNodeId(config.getNodeId())
                .setProcessConfig(config)
                .setTaskConfig(null)
                .setLevel(config.getLevel())
                .setSort(config.getSort())
                .setProcessName(config.getProcessName());

    }


    /**
     * 获取最高版本模型名称 构建模型
     * @param processName   模型名称
     * @return              流程模型
     */
    private ProcessDefinition processDefinition(String processName){
        Assert.notEmpty("openWorkflow by processName ,but processName is null! openWorkflow fail",processName);

        ProcessDefinition definition = this.repositoryService.processService().getProcessDefinitionByName(processName);
        Assert.notEmpty("processName is not exsit",definition);

        return definition;
    }


    private List<ProcessConfig> getProcessConfigList(String processId){
        return  this.repositoryService.processConfigurationService().queryList(new QueryFilter()
                             .setProcessId(processId));
    }

}
