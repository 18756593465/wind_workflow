package com.bcx.wind.workflow.entity;

import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.core.pojo.config.Condition;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.JsonHelper;
import com.bcx.wind.workflow.helper.ObjectHelper;

import java.util.*;

/**
 * 流程配置实体
 *
 * @author zhanglei
 */
public class ProcessConfig {

    /**
     * 配置主键
     */
    private String id;

    /**
     * 模型ID
     */
    private String processId;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 模型名称
     */
    private String processName;

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 条件
     */
    private String condition;

    /**
     * 节点配置
     */
    private String nodeConfig;

    /**
     * 审批人
     */
    private String approveUser;

    /**
     * 排序
     */
    private int sort;

    /**
     * 配置类型 1流程配置  2节点配置
     */
    private int level;



    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 条件匹配逻辑  并且 and   或者or
     */
    private String logic;


    private String businessConfig;

    /**
     * 匹配规则对象
     */
    private List<Condition>  conditions = new LinkedList<>();

    /**
     * 节点配置对象
     */
    private Map<String,Object> nodeConfigMap = new HashMap<>();

    /**
     * 业务条件配置
     */
    private Map<String,Object> businessConfigMap = new HashMap<>();


    /**
     * 审批人
     */
    private List<DefaultUser> approveUsers = new LinkedList<>();



    public String getId() {
        return id;
    }

    public ProcessConfig setId(String id) {
        this.id = id;
        return this;
    }

    public String getProcessId() {
        return processId;
    }

    public ProcessConfig setProcessId(String processId) {
        this.processId = processId;
        return this;
    }

    public String getConfigName() {
        return configName;
    }

    public ProcessConfig setConfigName(String configName) {
        this.configName = configName;
        return this;
    }

    public String getProcessName() {
        return processName;
    }

    public ProcessConfig setProcessName(String processName) {
        this.processName = processName;
        return this;
    }

    public String getNodeId() {
        return nodeId;
    }

    public ProcessConfig setNodeId(String nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public String getCondition() {
        return condition;
    }

    public ProcessConfig setCondition(String condition) {
        this.condition = condition;
        return this;
    }

    public String getNodeConfig() {
        return nodeConfig;
    }

    public ProcessConfig setNodeConfig(String nodeConfig) {
        this.nodeConfig = nodeConfig;
        return this;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public ProcessConfig setApproveUser(String approveUser) {
        this.approveUser = approveUser;
        return this;
    }

    public int getSort() {
        return sort;
    }

    public ProcessConfig setSort(int sort) {
        this.sort = sort;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public ProcessConfig setLevel(int level) {
        this.level = level;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public ProcessConfig setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getLogic() {
        return logic;
    }

    public ProcessConfig setLogic(String logic) {
        this.logic = logic;
        return this;
    }

    public String getBusinessConfig() {
        return businessConfig;
    }

    public ProcessConfig setBusinessConfig(String businessConfig) {
        this.businessConfig = businessConfig;
        return this;
    }

    public ProcessConfig addBusinessConfig(String key ,Object value){
        Assert.hasEmpty("data is empty!",key,value);

        this.businessConfigMap.put(key,value);
        this.businessConfig = JsonHelper.toJson(this.businessConfigMap);
        return this;
    }

    public ProcessConfig addNodeConfig(String key, Object value){
        Assert.hasEmpty("data is empty!",key,value);

        this.nodeConfigMap.put(key,value);
        this.nodeConfig = JsonHelper.toJson(this.nodeConfigMap);
        return this;
    }


    public ProcessConfig addCondition(Condition condition){
        Assert.notEmpty("data is empty!",condition);
        Assert.hasEmpty("data is empty!",condition.getKey(),condition.getCondition(),condition.getValue());

        this.conditions.add(condition);
        this.condition = JsonHelper.toJson(this.conditions);
        return this;
    }

    public ProcessConfig addApproveUser(DefaultUser defaultUser){
        Assert.notEmpty("data is empty!",defaultUser);
        Assert.hasEmpty("data is empty!",defaultUser.getUserId(),defaultUser.getUserName());

        this.approveUsers.add(defaultUser);
        this.approveUser = JsonHelper.toJson(this.approveUsers);
        return this;
    }


    @SuppressWarnings("unchecked")
    public List<Condition>  getConditions(){
        if(!ObjectHelper.isEmpty(this.conditions)){
            return conditions;
        }

        if(!ObjectHelper.isEmpty(this.condition)){
            this.conditions =  JsonHelper.parseJson(this.condition,List.class,Condition.class);
        }

        return conditions;
    }


    @SuppressWarnings("unchecked")
    public List<DefaultUser> getApproveUsers(){
        if(!ObjectHelper.isEmpty(this.approveUsers)){
            return approveUsers;
        }

        if(!ObjectHelper.isEmpty(this.approveUser)){
            this.approveUsers =  JsonHelper.parseJson(this.approveUser,List.class,DefaultUser.class);
        }

        return approveUsers;
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> getNodeConfigMap(){
        if(!ObjectHelper.isEmpty(this.nodeConfigMap)){
            return this.nodeConfigMap;
        }

        if(!ObjectHelper.isEmpty(this.nodeConfig)){
            this.nodeConfigMap = JsonHelper.parseJson(this.nodeConfig,Map.class,String.class,Object.class);
        }

        return this.nodeConfigMap;
    }

    public Map<String,Object>  getBusinessConfigMap(){
        if (!ObjectHelper.isEmpty(this.businessConfigMap)) {
            return this.businessConfigMap;
        }

        if(!ObjectHelper.isEmpty(this.businessConfig)){
            this.businessConfigMap = JsonHelper.parseJson(this.businessConfig,Map.class,String.class,Object.class);
        }

        return this.businessConfigMap;
    }

}
