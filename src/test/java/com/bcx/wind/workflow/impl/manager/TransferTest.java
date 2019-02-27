package com.bcx.wind.workflow.impl.manager;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.core.WorkflowService;
import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.core.pojo.TransferResult;
import org.junit.Test;

public class TransferTest  extends BaseTest {


    @Test
    public void transferOne(){

        WorkflowService service = engine.openWorkflowService();
        TransferResult result = service.transfer(new DefaultUser().setUserId("10003"),new DefaultUser().setUserId("10005").setUserName("季婷"));
        System.out.println();
        commit();
    }


}
