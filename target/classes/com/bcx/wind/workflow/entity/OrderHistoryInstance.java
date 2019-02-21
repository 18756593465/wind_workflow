package com.bcx.wind.workflow.entity;

import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.JsonHelper;
import com.bcx.wind.workflow.helper.ObjectHelper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 流程实例实体历史
 *
 * @author zhanglei
 */
public class OrderHistoryInstance  {

    /**
     * 流程实例号
     */
    private String id;

    /**
     * 模型ID
     */
    private String processId;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 期望完成时间
     */
    private String expireTime;

    /**
     * 父流程实例ID
     */
    private String parentId;

    /**
     * 版本
     */
    private int version;

    /**
     * 扩展数据
     */
    private String variable;

    /**
     * 业务数据
     */
    private String data;

    /**
     * 系统
     */
    private String system;

    /**
     * 流程变量集合
     */
    private Map<String,Object> variableMap = new HashMap<>();

    /**
     * 业务id 集合
     */
    private List<String> businessIds = new LinkedList<>();

    public List<String> getBusinessIds() {
        return businessIds;
    }

    public OrderHistoryInstance setBusinessIds(List<String> businessIds) {
        this.businessIds = businessIds;
        return this;
    }

    public Map<String, Object> getVariableMap() {
        if(!this.variable.isEmpty()){
            return this.variableMap;
        }

        if(!ObjectHelper.isEmpty(this.variable)){
            this.variableMap = JsonHelper.jsonToMap(this.variable);
        }

        return variableMap;
    }


    public OrderHistoryInstance  addValue(String key,Object value){
        Assert.hasEmpty("key or value is empty!",key,value);
        this.variableMap.put(key,value);

        this.variable = JsonHelper.toJson(this.variableMap);

        return this;
    }

    public OrderHistoryInstance removeKey(String key){
        Assert.hasEmpty("key is empty!",key);

        if(this.variableMap.containsKey(key)){
            this.variableMap.remove(key);
            this.variable = JsonHelper.toJson(this.variableMap);
        }
        return this;
    }

    public OrderHistoryInstance setVariableMap(Map<String, Object> variableMap) {
        this.variableMap = variableMap;
        return this;
    }

    public String getId() {
        return id;
    }

    public OrderHistoryInstance setId(String id) {
        this.id = id;
        return this;
    }

    public String getProcessId() {
        return processId;
    }

    public OrderHistoryInstance setProcessId(String processId) {
        this.processId = processId;
        return this;
    }


    public String getStatus() {
        return status;
    }

    public OrderHistoryInstance setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public OrderHistoryInstance setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public OrderHistoryInstance setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public OrderHistoryInstance setExpireTime(String expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public String getParentId() {
        return parentId;
    }

    public OrderHistoryInstance setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    public int getVersion() {
        return version;
    }

    public OrderHistoryInstance setVersion(int version) {
        this.version = version;
        return this;
    }

    public String getVariable() {
        return variable;
    }

    public OrderHistoryInstance setVariable(String variable) {
        this.variable = variable;
        this.variableMap.clear();
        return this;
    }

    public String getData() {
        return data;
    }

    public OrderHistoryInstance setData(String data) {
        this.data = data;
        return this;
    }

    public String getSystem() {
        return system;
    }

    public OrderHistoryInstance setSystem(String system) {
        this.system = system;
        return this;
    }
}
