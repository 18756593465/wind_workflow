package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.core.WorkflowSession;
import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.core.pojo.Workflow;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class WorkflowSessionRevokeTest extends BaseTest {


    @Test
    public void revoke(){

        WorkflowSession session = engine.openWorkflowSession("holiday");

        List<String>  businessIds = new LinkedList<>();
        businessIds.add("00001");
        Workflow workflow = session.revoke(businessIds,new DefaultUser().setUserId("10003"));

        System.out.println(workflow);
        assert workflow!=null;
        commit();
    }
}
