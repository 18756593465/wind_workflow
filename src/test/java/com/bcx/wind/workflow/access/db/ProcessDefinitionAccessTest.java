package com.bcx.wind.workflow.access.db;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.ProcessDefinition;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.TimeHelper;
import org.junit.Test;

import java.util.List;

public class ProcessDefinitionAccessTest extends BaseTest {

    private ProcessDefinition definition(){
        String content = "<process></process>";

        return new ProcessDefinition()
                .setId(ObjectHelper.primaryKey())
                .setProcessName("holiday")
                .setDisplayName("请假流程")
                .setStatus("1")
                .setVersion(1)
                .setCreateTime(TimeHelper.getNow())
                .setContent(content.getBytes())
                .setSystem("dms");
    }

    @Test
    public void add(){
        ProcessDefinition definition = definition();

        int ret = access.addProcess(definition);
        assert ret >= 1;

        commit();
    }


    @Test
    public void update(){
        ProcessDefinition definition = definition();
        access.addProcess(definition);

        definition.setSystem("eqms");
        int ret = access.updateProcess(definition);
        assert ret >= 1;

        commit();
    }

    @Test
    public void remove(){
        ProcessDefinition definition = definition();
        access.addProcess(definition);

        int ret = access.removeProcessById(definition.getId());
        assert ret >= 1;
    }

    @Test
    public void queryOne(){
        ProcessDefinition definition = definition();
        access.addProcess(definition);

        ProcessDefinition processDefinition = access.getProcessDefinitionById(definition.getId());
        assert processDefinition != null;
    }

    @Test
    public void query(){

        List<ProcessDefinition> processDefinitionList = access.selectProcessList(new QueryFilter());
        System.out.println(processDefinitionList.size());
    }

    @Test
    public void queryPage(){
        FlowPage<ProcessDefinition> page = new FlowPage<>();

        List<ProcessDefinition> list = access.selectProcessList(new QueryFilter(),page);
        System.out.println(list.size());
    }
}
