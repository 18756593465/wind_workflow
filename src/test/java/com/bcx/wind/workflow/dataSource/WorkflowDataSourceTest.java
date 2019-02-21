package com.bcx.wind.workflow.dataSource;

import com.bcx.wind.workflow.Access;
import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.dataSource.dataSource.WorkflowDataSource;
import com.bcx.wind.workflow.entity.TaskActor;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.jdbc.JdbcAccess;
import org.junit.Test;

import javax.sql.DataSource;

public class WorkflowDataSourceTest extends BaseTest {

    @Test
    public void workflowDataSourceTest(){
        DataSource dataSource = new WorkflowDataSource("org.postgresql.Driver","jdbc:postgresql://127.0.0.1:5432/zflow","postgres","253897");

        Access access = new JdbcAccess(dataSource);

        int ret = access.insertTaskActor(new TaskActor().setTaskId(ObjectHelper.primaryKey()).setTaskActorId("1003"));

        assert ret >= 1;
        commit();
    }
}
