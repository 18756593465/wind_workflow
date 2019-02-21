package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.core.WorkflowSession;
import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.core.pojo.Workflow;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.junit.Test;

public class WorkflowSessionRejectTest extends BaseTest {

    String orderId = "350b603772ec429287d953167f4286ed";

    @Test
    public void rejectOne(){
        ObjectHelper.primaryKey();
        WorkflowSession session = engine.openWorkflowSession("holiday");

        long start = System.currentTimeMillis();
        Workflow workflow = session.reject(orderId,"edit",new DefaultUser().setUserId("10003").setUserName("张三"));
        System.out.println(System.currentTimeMillis()-start+" 毫秒");
        assert workflow != null;

        commit();
    }



}
