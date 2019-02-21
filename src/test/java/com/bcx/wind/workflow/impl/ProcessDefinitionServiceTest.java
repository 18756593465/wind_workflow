package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.constant.ProcessType;
import com.bcx.wind.workflow.entity.ProcessDefinition;
import com.bcx.wind.workflow.io.Resources;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ProcessDefinitionServiceTest extends BaseTest {


    String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
            "\n" +
            "\n" +
            "<!DOCTYPE process PUBLIC \"-//workflow\" \"workflow-1-process.dtd\">\n" +
            "\n" +
            "<process name=\"holiday\" displayName=\"请假流程模型\">\n" +
            "    \n" +
            "    <start name=\"start\" displayName=\"开始\">\n" +
            "        <path name=\"startPath\" displayName=\"开始路线\" to=\"edit\"/>\n" +
            "    </start>\n" +
            "\n" +
            "    <task name=\"edit\" displayName=\"起草\">\n" +
            "        <path name=\"editPath\" displayName=\"起草路线\" to=\"router\"/>\n" +
            "    </task>\n" +
            "\n" +
            "    <router name=\"router\" displayName=\"子流程\">\n" +
            "        <start name=\"routerStart\" displayName=\"子流程开始\">\n" +
            "            <path name=\"routerStartPath\" displayName=\"子流程开始路线\" to=\"routerEdit\"/>\n" +
            "        </start>\n" +
            "\n" +
            "        <task name=\"routerEdit\" displayName=\"子流程起草\">\n" +
            "            <path name=\"routerEditPath\" displayName=\"子流程起草路线\" to=\"routerApprove\"/>\n" +
            "        </task>\n" +
            "        \n" +
            "        <task name=\"routerApprove\" displayName=\"子流程审批\">\n" +
            "            <path name=\"routerApprovePath\" displayName=\"子流程审批路线\" to=\"routerEnd\"/>\n" +
            "        </task>\n" +
            "        \n" +
            "        <end name=\"routerEnd\" displayName=\"子流程结束\"/>\n" +
            "\n" +
            "        <path name=\"router_path\" displayName=\"子流程路线\" to=\"scribe\"/>\n" +
            "    </router>\n" +
            "\n" +
            "    <scribe name=\"scribe\" displayName=\"订阅路由\">\n" +
            "        <path name=\"scribePath1\" displayName=\"订阅路线1\" to=\"scribeTask\"/>\n" +
            "        <path name=\"scribePath2\" displayName=\"订阅路线2\" to=\"monitor\"/>\n" +
            "    </scribe>\n" +
            "\n" +
            "    <scribeTask name=\"scribeTask\" displayName=\"订阅任务\"/>\n" +
            "\n" +
            "    <task name=\"monitor\" displayName=\"班长审批\" assigneeUser=\"minAssignee\" jointly=\"true\" interceptor=\"true\">\n" +
            "        <path name=\"monitorPath\" displayName=\"班长审批提交路线\" to=\"andFork\"/>\n" +
            "    </task>\n" +
            "\n" +
            "    <and name=\"andFork\" displayName=\"并且分支\">\n" +
            "        <path name=\"andFork_1\" displayName=\"并且分支路线1\" to=\"approveA\"/>\n" +
            "        <path name=\"andFork_2\" displayName=\"并且分支路线2\" to=\"approveC\"/>\n" +
            "    </and>\n" +
            "\n" +
            "    <task name=\"approveA\" displayName=\"审批A\">\n" +
            "        <path name=\"approveA_path\" displayName=\"审批A路线\" to=\"approveB\"/>\n" +
            "    </task>\n" +
            "\n" +
            "    <task name=\"approveC\" displayName=\"审批C\">\n" +
            "        <path name=\"approveC_path\" displayName=\"审批C路线\" to=\"andJoin\"/>\n" +
            "    </task>\n" +
            "\n" +
            "    <task name=\"approveB\" displayName=\"审批B\">\n" +
            "        <path name=\"approveB_path\" displayName=\"审批B路线\" to=\"andJoin\"/>\n" +
            "    </task>\n" +
            "\n" +
            "    <andJoin name=\"andJoin\" displayName=\"并且聚合\">\n" +
            "        <path name=\"andJoin_path\" displayName=\"并且聚合路线\" to=\"minister\"/>\n" +
            "    </andJoin>\n" +
            "\n" +
            "    <task name=\"minister\" displayName=\"部长审批\">\n" +
            "        <path name=\"minister_path\" displayName=\"部长审批路线\" to=\"disc\"/>\n" +
            "    </task>\n" +
            "\n" +
            "    <disc name=\"disc\" displayName=\"决策路由\">\n" +
            "        <path name=\"disc_path1\" displayName=\"决策路线1\" to=\"finance\"/>\n" +
            "        <path name=\"disc_path2\" displayName=\"决策路线2\" to=\"manager\"/>\n" +
            "    </disc>\n" +
            "\n" +
            "    <task name=\"finance\" displayName=\"财务审批\">\n" +
            "        <path name=\"finance_path\" displayName=\"财务路线\" to=\"boss\"/>\n" +
            "    </task>\n" +
            "\n" +
            "    <task name=\"manager\" displayName=\"总经理审批\">\n" +
            "        <path name=\"manager_path\" displayName=\"总经理路线\" to=\"end\"/>\n" +
            "    </task>\n" +
            "\n" +
            "    <task name=\"boss\" displayName=\"董事长\">\n" +
            "        <path name=\"boss_path\" displayName=\"董事长路线\" to=\"end\"/>\n" +
            "    </task>\n" +
            "\n" +
            "    <end name=\"end\" displayName=\"结束\"/>\n" +
            "\n" +
            "\n" +
            "    <assignee id=\"minAssignee\">\n" +
            "        <property userId=\"1001\" userName=\"张三\"/>\n" +
            "        <property userId=\"1002\" userName=\"李四\"/>\n" +
            "    </assignee>\n" +
            "</process>";

    private String definition() throws IOException {
        InputStream stream = Resources.getResourceAsStream("process/Context.xml");

        return engine.repositoryService().processService().deploy(stream,"dms");
    }


    private String definitionForStr() throws IOException {
        return engine.repositoryService().processService().deploy(xml,"dms");
    }

    @Test
    public void add() throws IOException {
        String processId = definition();
        assert processId != null;
    }

    @Test
    public void addForStr() throws IOException {
        String processId = definitionForStr();
        assert processId != null;
    }

    @Test
    public void queryOne() throws IOException {
        String processId = definitionForStr();
        ProcessDefinition definition = engine.repositoryService().processService().getProcessById(processId);
        assert definition != null;
    }

    @Test
    public void queryByProcessName() throws IOException {
        definitionForStr();

        ProcessDefinition definition = engine.repositoryService().processService().getProcessDefinitionByName("holiday");
        assert definition != null;
    }


    @Test
    public void deleteOne() throws IOException {
        String processId = definition();

        ProcessDefinition definition = access.getProcessDefinitionById(processId);
        definition.setStatus(ProcessType.RECOVERY);
        access.updateProcess(definition);

        int ret = engine.repositoryService().processService().deleteById(processId);
        assert ret >= 1;
    }

    @Test
    public void query(){
        List<ProcessDefinition> definitionList = engine.repositoryService().processService().queryList(new QueryFilter());
        System.out.println(definitionList.size());
    }

    @Test
    public void queryPage(){
        FlowPage<ProcessDefinition> page = new FlowPage<>();

        List<ProcessDefinition> definitions = engine.repositoryService().processService().queryList(new QueryFilter(),page);
        System.out.println(definitions.size());
    }

    @Test
    public void  release() throws IOException {
        String processId = definition();

        int ret = engine.repositoryService().processService().release(processId);
        assert ret >= 1;
        commit();
    }


    @Test
    public void recovery() throws IOException {
        String processId = definition();

        engine.repositoryService().processService().release(processId);
        int ret = engine.repositoryService().processService().recovery(processId);
        assert ret >= 1;
    }


}
