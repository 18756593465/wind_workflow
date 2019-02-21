package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.Access;
import com.bcx.wind.workflow.AccessService;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.ProcessConfigurationService;
import com.bcx.wind.workflow.core.flow.NodeModel;
import com.bcx.wind.workflow.entity.ProcessConfig;
import com.bcx.wind.workflow.entity.ProcessDefinition;
import com.bcx.wind.workflow.exception.WorkflowException;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.TimeHelper;
import com.bcx.wind.workflow.message.MessageHelper;
import com.bcx.wind.workflow.message.MsgConstant;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 流程配置模块
 */
public class ProcessConfigurationServiceImpl extends AccessService implements ProcessConfigurationService {

    private static final String PROCESS_CONFIG = "processConfig";

    public ProcessConfigurationServiceImpl(Access access) {
        super(access);
    }

    @Override
    public List<ProcessConfig> queryList(QueryFilter filter) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w001),filter);
        return access.selectProcessConfigList(filter);
    }

    @Override
    public int insert(ProcessConfig processConfig) {
        //校验
        checkProcessConfig(processConfig);
        //默认值
        processConfig.setId(ObjectHelper.primaryKey())
                .setCreateTime(TimeHelper.getNow());
        //校验数据合法
        checkProcessConfigData(processConfig);

        //设置配置等级
        setProcessConfigLevel(processConfig);

        //流程定义配置集合
        List<ProcessConfig> configList = getConfiglistByProcessId(processConfig.getProcessId());

        //设置排序
        int sort = changeSort(configList,processConfig,1);

        //执行新增
        return access.insertProcessConfig(processConfig.setSort(sort));
    }

    @Override
    public int update(ProcessConfig processConfig) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w002,PROCESS_CONFIG), processConfig.getId());
        //校验
        checkProcessConfig(processConfig);
        //校验合法
        checkProcessConfigData(processConfig);

        //配置等级
        setProcessConfigLevel(processConfig);

        //流程定义配置集合
        List<ProcessConfig> configList = getConfiglistByProcessId(processConfig.getProcessId());

        //设置排序
        int sort = changeSort(configList,processConfig,2);

        //执行更新
        return access.updateProcessConfig(processConfig.setSort(sort));
    }

    @Override
    public int deleteById(String id) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w002,PROCESS_CONFIG),id);

        ProcessConfig config = getProcessConfigById(id);
        if(ObjectHelper.isEmpty(config)){
           return 0;
        }

        //流程定义配置集合
        List<ProcessConfig>  configList = getConfiglistByProcessId(config.getProcessId());

        //更新排序
        changeSort(configList,config.setSort(configList.size()),2);

        //执行删除
        return access.removeProcessConfigById(id);
    }

    @Override
    public ProcessConfig getProcessConfigById(String id) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w002,PROCESS_CONFIG),id);

        return access.getProcessConfigById(id);
    }

    private List<ProcessConfig> getConfiglistByProcessId(String processId){
        return queryList(new QueryFilter().setProcessId(processId));
    }

    private void checkProcessConfig(ProcessConfig config){
        Assert.notEmpty("processConfig is null!",config);
        Assert.hasEmpty(MessageHelper.getMsg(MsgConstant.w003,PROCESS_CONFIG),config.getProcessId(),config.getConfigName(),config.getProcessName(),
                config.getNodeId());
        if(!ObjectHelper.isEmpty(config.getApproveUser())) {
            config.getApproveUsers();
        }

        if(!ObjectHelper.isEmpty(config.getCondition())){
            config.getConditions();
        }

        if(!ObjectHelper.isEmpty(config.getNodeConfig())){
            config.getNodeConfigMap();
        }
    }


    private void checkProcessConfigData(ProcessConfig config){
        String processId = config.getProcessId();
        //检验流程定义是否存在
        ProcessDefinition definition = engine().repositoryService().processService().getProcessById(processId);
        Assert.notEmpty("processId is not exist!",definition);

        //校验节点是否存在
        List<NodeModel> taskNodes = definition.getProcessModel().getAllTaskNodes();
        String processName = definition.getProcessName();

        Assert.isTrue("processName is not true!",!processName.equals(config.getProcessName()));
        if(config.getNodeId().equals(config.getProcessName())){
            return;
        }

        boolean nodeExist = false;
        for(NodeModel nodeModel : taskNodes){
            if(nodeModel.name().equals(config.getNodeId())){
                nodeExist = true;
                break;
            }
        }
        Assert.isTrue(MessageHelper.getMsg(MsgConstant.w004,processName,config.getNodeId()),!nodeExist);
    }


    private  void setProcessConfigLevel(ProcessConfig config){
        config.setLevel(config.getNodeId().equals(config.getProcessName()) ? 1 : 2);
    }


    /**
     * 设置排序
     * @param configList  节点下所有配置
     * @param config      当前配置
     * @param operate     操作  新增1  更新2
     * @return            排序
     */
    private Integer changeSort(List<ProcessConfig> configList,ProcessConfig config , int operate){
        //设置排序初始值
        int maxSort = 1;

        //如果节点下配置为空， 则表示第一个新增配置,直接返回
        if(ObjectHelper.isEmpty(configList)){
            return maxSort;
        }

        //获取节点下配置集合中排序最高的
        maxSort = getMaxSort(configList);

        //如果操作为添加 排序+1  直接返回
        if(operate==1){
            return maxSort+1;
        }


        //更新  获取需要更新的配置排序  更新前排序
        int start = getStartConfigSort(configList,config);
        //如果排序字段为空则不修改
        if(ObjectHelper.isEmpty(config.getSort()) || config.getSort()==0){
            return start;
        }

        //更新后的排序
        int end = config.getSort();

        //如果更新后的排序大于最大排序，则为最大排序。如果小于1 则为1
        end = end > maxSort ? maxSort : end;
        end = end < 1 ? 1 : end;

        //如果更新后和更新前相等，则返回
        if(end==start){
            return end;
        }

        //获得单个操作指向
        int add = start > end ? 1 : -1;

        //获取需要更新的实施配置ID集合
        List<String> configIds = new LinkedList<>();
        for(ProcessConfig con : configList){
            int sort = con.getSort();

            //如果 指向为1 表示从下往上更新排序,需要将开始上面结束下面包含结束的配置数据排序下移
            if(add==1 && sort<start && sort>=end){
                configIds.add(con.getId());
            }

            if(add==-1 && sort>start && sort<=end){
                configIds.add(con.getId());
            }

        }

        //更新sort
        access.updateSort(configIds,add);

        return end;
    }

    private Integer  getStartConfigSort(List<ProcessConfig> configList, ProcessConfig config){
        return configList.stream().filter(c->c.getId().equals(config.getId()))
                .findFirst().map(ProcessConfig::getSort)
                .orElseThrow(()->new WorkflowException("processConfig is not Exist"));
    }

    private Integer  getMaxSort(List<ProcessConfig> configList){
        return configList.stream().max(Comparator.comparingInt(ProcessConfig::getLevel))
                .map(ProcessConfig::getSort)
                .orElseThrow(()->new WorkflowException("get max sort from configList fail! because data is error"));
    }

}
