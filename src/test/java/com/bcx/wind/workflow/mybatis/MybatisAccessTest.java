package com.bcx.wind.workflow.mybatis;

import com.bcx.wind.workflow.Access;
import com.bcx.wind.workflow.WorkflowEngine;
import com.bcx.wind.workflow.dataSource.transaction.AccessTransactionManager;
import com.bcx.wind.workflow.impl.WorkflowEngineBuilder;

import com.bcx.wind.workflow.mybatis.MybatisAccess;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;

public class MybatisAccessTest {

    @Test
    public void mybatisAccessTest (){
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("mybatis/mybatis-config.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(stream);
        Access access = new MybatisAccess(factory);

        WorkflowEngine engine = WorkflowEngineBuilder.builder(access);

        AccessTransactionManager transactionManager = AccessTransactionManager.getInstance();

        transactionManager.commit();

    }
}
