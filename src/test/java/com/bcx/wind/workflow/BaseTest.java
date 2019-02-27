package com.bcx.wind.workflow;

import com.bcx.wind.workflow.dataSource.dataSource.WorkflowDataSource;
import com.bcx.wind.workflow.dataSource.transaction.AccessTransactionManager;
import com.bcx.wind.workflow.impl.WorkflowEngineBuilder;
import com.bcx.wind.workflow.jdbc.JdbcAccess;
import org.junit.Before;

public class BaseTest {

    protected WorkflowEngine engine;


    protected String orderId = "533e3b7ffec644f78d6316886d18f346";


    protected  Access access;

    @Before
    public void  engine(){
        WorkflowDataSource dataSource = new WorkflowDataSource("org.postgresql.Driver","jdbc:postgresql://127.0.0.1:5432/zflow","postgres","253897");
        this.access = new JdbcAccess(dataSource);

        this.engine = WorkflowEngineBuilder.builder(access);
    }


    protected void commit(){
        AccessTransactionManager.getInstance().commit();
    }
}
