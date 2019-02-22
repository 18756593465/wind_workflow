package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.core.WorkflowSession;
import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.core.pojo.Workflow;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.junit.Test;

public class WorkflowSessionCompleteTest extends BaseTest {



    @Test
    public void  complete(){

        ObjectHelper.primaryKey();

        WorkflowSession session = engine.openWorkflowSession("holiday");
        Workflow workflow = session.complete(orderId,new DefaultUser().setUserId("10003").setUserName("张三"));

        assert workflow!=null;
        commit();
    }
}
