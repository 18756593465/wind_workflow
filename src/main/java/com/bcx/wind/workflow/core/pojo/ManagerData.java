package com.bcx.wind.workflow.core.pojo;

import com.bcx.wind.workflow.WorkflowEngine;

/**
 * 工作流管理服务 相关数据对象
 *
 * @author zhagnlei
 */
public class ManagerData {

    private WorkflowEngine engine;


    public WorkflowEngine getEngine() {
        return engine;
    }

    public ManagerData setEngine(WorkflowEngine engine) {
        this.engine = engine;
        return this;
    }
}
