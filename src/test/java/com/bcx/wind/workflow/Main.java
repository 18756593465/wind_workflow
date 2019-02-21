package com.bcx.wind.workflow;

import com.bcx.wind.workflow.WorkflowEngine;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.dataSource.transaction.AccessTransactionManager;
import com.bcx.wind.workflow.entity.ProcessDefinition;
import com.bcx.wind.workflow.jdbc.WorkflowSelfBuilder;


import java.util.List;

public class Main {

    public static void main(String[] args) {
        AccessTransactionManager transactionManager = AccessTransactionManager.getInstance();

        WorkflowEngine workflowEngine = WorkflowSelfBuilder.buildEngine("org.postgresql.Driver","jdbc:postgresql://127.0.0.1:5432/zflow",
                "postgres","253897");


        workflowEngine.runtimeService().orderService().queryList(null);
        FlowPage<ProcessDefinition> page  = new FlowPage<>();
        page.setPageNum(1);
        page.setPageSize(3);
        List<ProcessDefinition> definitionList = workflowEngine.repositoryService().processService().queryList(new QueryFilter(), page);
   System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
           System.out.println();
        System.out.println();
        System.out.println();

        System.out.println();
    }

}
