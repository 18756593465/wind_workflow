package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.Access;
import com.bcx.wind.workflow.AccessService;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.ProcessService;
import com.bcx.wind.workflow.core.constant.ProcessType;
import com.bcx.wind.workflow.core.flow.NodeModel;
import com.bcx.wind.workflow.core.flow.ProcessModel;
import com.bcx.wind.workflow.core.flow.TaskNode;
import com.bcx.wind.workflow.entity.ProcessConfig;
import com.bcx.wind.workflow.entity.ProcessDefinition;
import com.bcx.wind.workflow.exception.WorkflowException;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.StreamHelper;
import com.bcx.wind.workflow.helper.TimeHelper;
import com.bcx.wind.workflow.message.MessageHelper;
import com.bcx.wind.workflow.parser.ProcessBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.List;

import static com.bcx.wind.workflow.core.constant.Constant.AND;
import static com.bcx.wind.workflow.core.constant.Constant.ARRAY;
import static com.bcx.wind.workflow.core.constant.Constant.JSON;
import static com.bcx.wind.workflow.message.MsgConstant.*;

/**
 * 流程定义模块
 *
 * @author zhanglei
 */
public class ProcessServiceImpl extends AccessService implements ProcessService {

    private static final Logger logger = LoggerFactory.getLogger(ProcessServiceImpl.class);


    public ProcessServiceImpl(Access access) {
        super(access);
    }


    @Override
    @SuppressWarnings("unchecked")
    public String deploy(String processContent,String system) {
        Assert.notEmpty("process deploy processContent data is null!",processContent);
        //获取流程定义ID
        String processId = ObjectHelper.primaryKey();

        //获取版本
        List<ProcessDefinition> definitions = getProcessList(processContent);
        ProcessDefinition maxVersionProcess = getMaxVersionProcess(definitions);
        int version = getMaxVersion(maxVersionProcess);

        //构建模型
        ProcessModel processModel = ProcessBuilder.getInstance().setContext(processContent).build(processId);


        //构建流程定义
        ProcessDefinition processDefinition = buildProcessDefinition(processModel,processId
                ,version,processModel.getProcessId(),processContent,system);

        //新增
        access.addProcess(processDefinition);

        //返回
        return processId;
    }




    private List<ProcessDefinition> getProcessList(String processContent){
        //获取模型名称
        String processName = ProcessBuilder.getInstance().setContext(processContent).getProcessName();

        //通过名称获取旧版流程定义
        return  queryList(new QueryFilter().setProcessName(processName));
    }


    /**
     * 获取最高版本
     */
    private int getMaxVersion(ProcessDefinition processDefinition){
        if(ObjectHelper.isEmpty(processDefinition)){
            return 1;
        }

        return processDefinition.getVersion()+1;
    }


    /**
     * 获取最高版本模型
     * @param definitions  模型集合
     * @return             模型
     */
    @SuppressWarnings("unchecked")
    private ProcessDefinition getMaxVersionProcess(List<ProcessDefinition> definitions){
        return ObjectHelper.isEmpty(definitions) ? null : definitions.stream().max(Comparator.comparingInt(ProcessDefinition::getVersion))
                .orElseThrow(()->new WorkflowException("get max version process error !"));
    }


    /**
     * 构建processDefinition
     * @param model       流程模型
     * @param processId   流程ID
     * @return            流程定义
     */
    private ProcessDefinition buildProcessDefinition(ProcessModel model , String processId,int version,String parentId,String content,String system){
        return new ProcessDefinition()
                .setId(processId)
                .setProcessName(model.getName())
                .setDisplayName(model.getDisplayName())
                .setStatus(ProcessType.EDIT)
                .setVersion(version)
                .setParentId(parentId)
                .setCreateTime(TimeHelper.getNow())
                .setContent(content.getBytes())
                .setSystem(system);

    }

    @Override
    public String deploy(InputStream inputStream,String system) {
        Assert.notEmpty("inputStream is null!",inputStream);
        try {
            String content = new String(StreamHelper.getByte(inputStream),"utf-8");
            return deploy(content,system);
        } catch (UnsupportedEncodingException e) {
            if(logger.isDebugEnabled()){
                logger.debug(e.getMessage(),e);
            }
            throw new WorkflowException("deploy process stream is error !");
        }
    }

    @Override
    public ProcessDefinition getProcessDefinitionByName(String processName) {
        Assert.notEmpty("getProcessDefinitionByName fail ,because processName is null",processName);

        List<ProcessDefinition> definitions = this.queryList(new QueryFilter().setProcessName(processName));

        if(!ObjectHelper.isEmpty(definitions)){
            ProcessDefinition definition =  definitions.stream().max(Comparator.comparingInt(ProcessDefinition::getVersion))
                    .orElseThrow(()->new WorkflowException("processDefinition version is null ,data error"));

            ProcessModel model = ProcessBuilder.getInstance().setContext(definition.content()).build(definition.getId());
            Assert.notEmpty("build processModel error !",model);

            return definition.setProcessModel(model);
        }

        return null;
    }


    @Override
    public List<ProcessDefinition> queryList(QueryFilter queryFilter, FlowPage<ProcessDefinition> flowPage) {
        Assert.notEmpty(MessageHelper.getMsg(w001),queryFilter);
        return getAccess().selectProcessList(queryFilter,flowPage);
    }

    @Override
    public List<ProcessDefinition> queryList(QueryFilter queryFilter) {
        Assert.notEmpty(MessageHelper.getMsg(w001),queryFilter);
        return access.selectProcessList(queryFilter);
    }


    @Override
    public ProcessDefinition getProcessById(String processId) {
        Assert.notEmpty(MessageHelper.getMsg(w002,processId),processId);

        //查询
        ProcessDefinition definition = access.getProcessDefinitionById(processId);
        Assert.notEmpty("from processId search process is null!",definition);

        ProcessModel model = ProcessBuilder.getInstance().setContext(definition.content()).build(processId);
        Assert.notEmpty("build processModel error !",model);

        definition.setProcessModel(model);
        return definition;
    }


    /**
     * 删除工作流 需要满足 1、改流程定义下没有正在执行的流程，2、改流程定义必须为回收状态
     * @param processId     流程定义ID
     * @return              删除结果
     */
    @Override
    public int deleteById(String processId) {
        ProcessDefinition definition = getProcessById(processId);

        if(ProcessType.RECOVERY.equals(definition.getStatus())){
            return access.removeProcessById(processId);
        }

        throw new WorkflowException(MessageHelper.getMsg(w007,processId));
    }


    /**
     * 发布工作流  工作流状态必须为编辑 才可发布  主要操作是将状态改成发布状态
     * @param processId    流程定义ID
     * @return             发布结果
     */
    @Override
    public int release(String processId) {
        ProcessDefinition definition = getProcessById(processId);

        if(ProcessType.EDIT.equals(definition.getStatus())){
            //添加默认流程节点配置\
            addDefaultProcessConfig(definition.getProcessModel());

            definition.setStatus(ProcessType.RELEASE);
            return access.updateProcess(definition);
        }

        throw new WorkflowException(MessageHelper.getMsg(w008,processId));
    }

    private void addDefaultProcessConfig(ProcessModel model){
        List<NodeModel> taskNodes = model.getAllTaskNodes();

        //添加流程配置
        ProcessConfig processConfig = createProcessConfig(model);
        engine().repositoryService().processConfigurationService().insert(processConfig);

        //添加节点配置
        for(NodeModel nodeModel : taskNodes){
            ProcessConfig nodeConfig = createNodeConfig(nodeModel,model);
            engine().repositoryService().processConfigurationService().insert(nodeConfig);
        }
    }


    /**
     * 创建默认流程配置数据
     *
     * @param processModel  流程模型
     * @return              流程配置对象
     */
    private ProcessConfig createProcessConfig(ProcessModel processModel){
        return new ProcessConfig()
                .setId(ObjectHelper.primaryKey())
                .setProcessId(processModel.getProcessId())
                .setConfigName(processModel.getDisplayName())
                .setProcessName(processModel.getName())
                .setNodeId(processModel.getName())
                .setCondition(ARRAY)
                .setNodeConfig(JSON)
                .setApproveUser(ARRAY)
                .setSort(1)
                .setLevel(1)
                .setCreateTime(TimeHelper.getNow())
                .setLogic(AND);
    }


    /**
     * 创建节点配置
     *
     * @param nodeModel      节点模型
     * @param processModel   流程模型
     * @return               节点配置对象
     */
    private ProcessConfig createNodeConfig(NodeModel nodeModel,ProcessModel processModel){
        return new ProcessConfig()
                .setId(ObjectHelper.primaryKey())
                .setProcessId(processModel.getProcessId())
                .setConfigName(nodeModel.displayName())
                .setProcessName(processModel.getName())
                .setNodeId(nodeModel.name())
                .setCondition(ARRAY)
                .setNodeConfig(JSON)
                .setApproveUser(ARRAY)
                .setSort(1)
                .setLevel(2)
                .setCreateTime(TimeHelper.getNow())
                .setLogic(AND);
    }



    /**
     * 回收工作流  1、确保状态为发布 2、确保该流程下没有正在执行的流程
     * @param processId  流程定义ID
     * @return           回收结果
     */
    @Override
    public int recovery(String processId) {
        ProcessDefinition definition = getProcessById(processId);

        if(ProcessType.RELEASE.equals(definition.getStatus())){
            definition.setStatus(ProcessType.RECOVERY);
            return access.updateProcess(definition);
        }
        throw new WorkflowException(MessageHelper.getMsg(w009,processId));
    }


}
