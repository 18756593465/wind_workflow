package com.bcx.wind.workflow.spring;

import com.bcx.wind.workflow.entity.ProcessDefinition;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.UUID;

public class SpringAccessTest {

    @Test
    public void springAccessTest() {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanConfig.class);
        TestService testService = context.getBean(TestService.class);

        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setId(UUID.randomUUID().toString().replaceAll("-",""));
        processDefinition.setProcessName("holiday");
        String content = "";
        testService.insert(processDefinition);
    }
}
