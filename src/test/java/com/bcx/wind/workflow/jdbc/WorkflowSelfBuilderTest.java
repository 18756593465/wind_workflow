package com.bcx.wind.workflow.jdbc;

import com.bcx.wind.workflow.WorkflowEngine;
import org.junit.Test;

public class WorkflowSelfBuilderTest {

    @Test
    public void buildEngine(){

        WorkflowEngine engine = WorkflowSelfBuilder.buildEngine("org.postgresql,Driver","jdbc:postgresql://127.0.0.1:5432/zflow","postgres","253897");
        System.out.println(engine);
    }
}
