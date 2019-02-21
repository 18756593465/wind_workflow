package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.ProcessConfig;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.TimeHelper;
import com.bcx.wind.workflow.io.Resources;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ProcessConfigServiceTest extends BaseTest {

    private String  definition() throws IOException {
        InputStream stream = Resources.getResourceAsStream("process/Context.xml");

        return engine.repositoryService().processService().deploy(stream,"dms");
    }


    private ProcessConfig config(String processId){
        return new ProcessConfig()
                .setId(ObjectHelper.primaryKey())
                .setProcessId(processId)
                .setConfigName("配置名称")
                .setProcessName("holiday")
                .setNodeId("holiday")
                .setCondition("[]")
                .setNodeConfig("{}")
                .setApproveUser("[]")
                .setSort(1)
                .setLevel(1)
                .setCreateTime(TimeHelper.getNow())
                .setLogic("and");
    }

    @Test
    public void add() throws IOException {
        String processId = definition();

        ProcessConfig config = config(processId);
        int ret = engine.repositoryService().processConfigurationService().insert(config);
        assert ret >= 1;
        commit();
    }

    @Test
    public void update() throws IOException {
        String processId = definition();

        ProcessConfig config = config(processId);
        engine.repositoryService().processConfigurationService().insert(config);

        config.setConfigName("配置名称修改");
        int ret = engine.repositoryService().processConfigurationService().update(config);
        assert ret >= 1;
        commit();
    }


    @Test
    public void remove() throws IOException {
        String processId = definition();

        ProcessConfig config = config(processId);
        engine.repositoryService().processConfigurationService().insert(config);

        int ret = engine.repositoryService().processConfigurationService().deleteById(config.getId());
        assert ret >= 1;
    }


    @Test
    public void queryOne() throws IOException {
        String processId = definition();

        ProcessConfig config = config(processId);
        engine.repositoryService().processConfigurationService().insert(config);

        ProcessConfig processConfig = engine.repositoryService().processConfigurationService().getProcessConfigById(config.getId());
        assert processConfig!=null;
    }

    @Test
    public void query(){

        List<ProcessConfig> configList = engine.repositoryService().processConfigurationService().queryList(new QueryFilter());
        System.out.println(configList.size());
    }


}
