package com.bcx.wind.workflow.core.pojo;

import com.bcx.wind.workflow.core.pojo.config.Condition;
import com.bcx.wind.workflow.entity.ProcessConfig;

import java.util.LinkedList;
import java.util.List;

/**
 * @author zhanglei
 */
public class Configuration {

    /**
     * 流程配置
     */
    private ProcessConfig processConfig;


    /**
     * 流程所有配置数据
     */
    private List<ProcessConfig> configList;


    /**
     * 流程所有配置数据解析数据
     */
    private List<Configuration> configurationList = new LinkedList<>();

    
    /**
     * 当前节点配置
     */
    private ProcessConfig  taskConfig;
    
    
    private  String processId;
    
    private String configName;
    
    private String processName;
    
    private String nodeId;

    private int level;

    private int sort;

    /**
     * 匹配规则配置
     */
    private List<Condition> conditionConfig = new LinkedList<>();

    /**
     * 当前节点配置
     */
    private NodeConfig nodeConfig;

    /**
     * 审批人配置
     */
    private List<DefaultUser> approveUser = new LinkedList<>();

    /**
     * 匹配逻辑  and   or
     */
    private String logic;

    public ProcessConfig getProcessConfig() {
        return processConfig;
    }

    public Configuration addConfigurationList(Configuration configuration){
        this.configurationList.add(configuration);
        return this;
    }

    public Configuration setProcessConfig(ProcessConfig processConfig) {
        this.processConfig = processConfig;
        return this;
    }

    public List<ProcessConfig> getConfigList() {
        return configList;
    }

    public Configuration setConfigList(List<ProcessConfig> configList) {
        this.configList = configList;
        return this;
    }

    public ProcessConfig getTaskConfig() {
        return taskConfig;
    }

    public Configuration setTaskConfig(ProcessConfig taskConfig) {
        this.taskConfig = taskConfig;
        return this;
    }

    public String getProcessId() {
        return processId;
    }

    public Configuration setProcessId(String processId) {
        this.processId = processId;
        return this;
    }

    public String getConfigName() {
        return configName;
    }

    public Configuration setConfigName(String configName) {
        this.configName = configName;
        return this;
    }

    public String getProcessName() {
        return processName;
    }

    public Configuration setProcessName(String processName) {
        this.processName = processName;
        return this;
    }

    public String getNodeId() {
        return nodeId;
    }

    public Configuration setNodeId(String nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public List<Condition> getConditionConfig() {
        return conditionConfig;
    }

    public Configuration setConditionConfig(List<Condition> conditionConfig) {
        this.conditionConfig = conditionConfig;
        return this;
    }

    public NodeConfig getNodeConfig() {
        return nodeConfig;
    }

    public Configuration setNodeConfig(NodeConfig nodeConfig) {
        this.nodeConfig = nodeConfig;
        return this;
    }

    public List<Configuration> getConfigurationList() {
        return configurationList;
    }

    public Configuration setConfigurationList(List<Configuration> configurationList) {
        this.configurationList = configurationList;
        return this;
    }

    public List<DefaultUser> getApproveUser() {
        return approveUser;
    }

    public Configuration setApproveUser(List<DefaultUser> approveUser) {
        this.approveUser = approveUser;
        return this;
    }

    public String getLogic() {
        return logic;
    }

    public Configuration setLogic(String logic) {
        this.logic = logic;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public Configuration setLevel(int level) {
        this.level = level;
        return this;
    }

    public int getSort() {
        return sort;
    }

    public Configuration setSort(int sort) {
        this.sort = sort;
        return this;
    }
}
