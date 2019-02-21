package com.bcx.wind.workflow.access.db;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.ProcessConfig;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.junit.Test;

import java.util.List;

public class ProcessConfigAccessTest extends BaseTest {

    private ProcessConfig config(){
        return new ProcessConfig()
                .setId(ObjectHelper.primaryKey())
                .setProcessId(ObjectHelper.primaryKey())
                .setConfigName("配置名称")
                .setProcessName("holiday")
                .setNodeId("approve")
                .setCondition("[]")
                .setNodeConfig("{}")
                .setApproveUser("[]")
                .setSort(1)
                .setLevel(2)
                .setCreateTime("2019-01-04 12:32:43")
                .setLogic("and");
    }

    @Test
    public void add(){
        ProcessConfig config = config();

        int ret = access.insertProcessConfig(config);
        assert ret >= 1;
    }


    @Test
    public void update(){
        ProcessConfig config = config();
        access.insertProcessConfig(config);

        config.setProcessName("demo");
        int ret = access.updateProcessConfig(config);
        assert ret >= 1;
    }

    @Test
    public void remove(){
        ProcessConfig config = config();
        access.insertProcessConfig(config);

        int ret = access.removeProcessConfigById(config.getId());
        assert ret >= 1;
    }


    @Test
    public void queryOne(){
        ProcessConfig config = config();
        access.insertProcessConfig(config);

        ProcessConfig processConfig = access.getProcessConfigById(config.getId());
        assert processConfig !=null ;
    }

    @Test
    public void query(){
        QueryFilter filter = new QueryFilter();
        List<ProcessConfig> configList = access.selectProcessConfigList(filter);
        System.out.println(configList.size());
    }
}
