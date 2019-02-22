package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.core.WorkflowSession;
import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.core.pojo.Workflow;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.junit.Test;

public class WorkflowSessionRejectTest extends BaseTest {

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


    @Test
    public void rejectTwo(){
        ObjectHelper.primaryKey();
        WorkflowSession session = engine.openWorkflowSession("holiday");

        long start = System.currentTimeMillis();
        Workflow workflow = session.reject(orderId,"approveA",new DefaultUser().setUserId("100011").setUserName("孙悟空"));
        System.out.println(System.currentTimeMillis()-start+" 毫秒");
        assert workflow != null;

        commit();
    }


    @Test
    public void rejectThree(){
        ObjectHelper.primaryKey();
        WorkflowSession session = engine.openWorkflowSession("holiday");

        long start = System.currentTimeMillis();
        Workflow workflow = session.reject(orderId,"routerApprove",new DefaultUser().setUserId("10005").setUserName("孙悟空"));
        System.out.println(System.currentTimeMillis()-start+" 毫秒");
        assert workflow != null;

        commit();
    }


    @Test
    public void rejectFore(){
        ObjectHelper.primaryKey();
        WorkflowSession session = engine.openWorkflowSession("holiday");

        long start = System.currentTimeMillis();
        Workflow workflow = session.reject(orderId,"edit",new DefaultUser().setUserId("100012").setUserName("孙悟空"));
        System.out.println(System.currentTimeMillis()-start+" 毫秒");
        assert workflow != null;
        commit();
    }

}
