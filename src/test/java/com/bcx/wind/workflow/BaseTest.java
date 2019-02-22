package com.bcx.wind.workflow;

import com.bcx.wind.workflow.dataSource.dataSource.WorkflowDataSource;
import com.bcx.wind.workflow.dataSource.transaction.AccessTransactionManager;
import com.bcx.wind.workflow.impl.WorkflowEngineBuilder;
import com.bcx.wind.workflow.jdbc.JdbcAccess;
import org.junit.Before;

public class BaseTest {

    protected WorkflowEngine engine;


    protected String orderId = "b0ee8c043f584487a0fd08b2de87715a";


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
