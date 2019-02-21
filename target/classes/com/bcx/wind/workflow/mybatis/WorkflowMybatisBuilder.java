package com.bcx.wind.workflow.mybatis;

import com.bcx.wind.workflow.Access;
import com.bcx.wind.workflow.WorkflowEngine;
import com.bcx.wind.workflow.impl.WorkflowEngineBuilder;
import org.apache.ibatis.session.SqlSessionFactory;

public class WorkflowMybatisBuilder {

    private WorkflowMybatisBuilder(){

    }

    public static WorkflowEngine buildEngine(SqlSessionFactory sqlSessionFactory){
        synchronized (WorkflowMybatisBuilder.class){
            Access access = new MybatisAccess(sqlSessionFactory);
            return WorkflowEngineBuilder.builder(access);
        }
    }
}
