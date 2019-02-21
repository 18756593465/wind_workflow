package com.bcx.wind.workflow.entity;

import com.bcx.wind.workflow.core.flow.ProcessModel;
import com.bcx.wind.workflow.exception.WorkflowException;

import java.io.UnsupportedEncodingException;

/**
 * 流程定义，采用xml方式记录流程执行模板，在流程定义名称下，编辑流程定义做版本升级
 *
 * @author zhanglei
 */
public class ProcessDefinition {

    private static final long  serialVersionUID = 827597049810948104L;

    /**
     * 流程定义主键
     */
    private String id;

    /**
     * 流程定义名称
     */
    private String processName;

    /**
     * 流程定义中文名
     */
    private String displayName;

    /**
     * 状态     编辑edit  发布release  回收recovery
     */
    private String status;

    /**
     * 父ID
     */
    private String parentId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 版本   相同名称的多个流程定义以版本区分
     */
    private int version;

    /**
     * 流程定义内容  xml格式二进制数据
     */
    private byte[]  content;

    /**
     * 系统
     */
    private String system;


    /**
     * 流程定义模型  不属于表单字段
     */
    private ProcessModel processModel;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public ProcessDefinition setId(String id) {
        this.id = id;
        return this;
    }

    public String getProcessName() {
        return processName;
    }

    public ProcessDefinition setProcessName(String processName) {
        this.processName = processName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ProcessDefinition setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ProcessDefinition setStatus(String status) {
        this.status = status;
        return this;
    }

    public int getVersion() {
        return version;
    }

    public ProcessDefinition setVersion(int version) {
        this.version = version;
        return this;
    }

    public byte[] getContent() {
        return content;
    }

    public String content(){
        try {
            return new String(content,"utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new WorkflowException(e.getMessage());
        }
    }

    public ProcessDefinition setContent(byte[] content) {
        this.content = content;
        return this;
    }

    public String getParentId() {
        return parentId;
    }

    public ProcessDefinition setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public ProcessDefinition setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getSystem() {
        return system;
    }

    public ProcessDefinition setSystem(String system) {
        this.system = system;
        return this;
    }

    public ProcessModel getProcessModel() {
        return processModel;
    }

    public ProcessDefinition setProcessModel(ProcessModel processModel) {
        this.processModel = processModel;
        return this;
    }
}
