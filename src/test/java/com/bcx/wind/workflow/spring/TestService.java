package com.bcx.wind.workflow.spring;

import com.bcx.wind.workflow.WorkflowEngine;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestService {

    @Autowired
    private WorkflowEngine engine;

    @Transactional
    public void insert(ProcessDefinition processDefinition){

        System.out.println(engine);
    }


}
