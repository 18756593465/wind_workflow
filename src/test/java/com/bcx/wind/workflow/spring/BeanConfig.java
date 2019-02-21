package com.bcx.wind.workflow.spring;

import com.bcx.wind.workflow.BeanContext;
import com.bcx.wind.workflow.WorkflowEngine;
import com.bcx.wind.workflow.dataSource.dataSource.WorkflowDataSource;
import com.bcx.wind.workflow.impl.WorkflowEngineBuilder;
import com.bcx.wind.workflow.spring.SpringAccess;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.bcx.wind.workflow.spring")
public class BeanConfig {

    @Bean
    public WorkflowDataSource dataSource(){
        WorkflowDataSource dataSource = new WorkflowDataSource("org.postgresql.Driver","jdbc:postgresql://127.0.0.1:5432/zflow","postgres","253897");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager  platformTransactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SpringAccess springAccess(){
        SpringAccess access = new SpringAccess();
        access.setDataSource(dataSource());
        return access;
    }

    @Bean
    public WorkflowEngine workflowEngine(){
        WorkflowEngine engine =  WorkflowEngineBuilder.builder(springAccess());
        engine.context(new BeanContext());

        return engine;
    }
}
