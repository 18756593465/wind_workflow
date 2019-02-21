package com.bcx.wind.workflow.jdbc;

import com.bcx.wind.workflow.Access;
import com.bcx.wind.workflow.WorkflowEngine;
import com.bcx.wind.workflow.dataSource.dataSource.WorkflowDataSource;
import com.bcx.wind.workflow.impl.WorkflowEngineBuilder;


/**
 * 工作流构建器
 */
public class WorkflowSelfBuilder {

    private WorkflowSelfBuilder(){}


    public static WorkflowEngine buildEngine(String driver,String url,String user,String password){
        synchronized (WorkflowSelfBuilder.class){
            WorkflowDataSource dataSource = new WorkflowDataSource(driver,url, user,password);
            Access access = new JdbcAccess(dataSource);
            return  WorkflowEngineBuilder.builder(access);
        }
    }
}
