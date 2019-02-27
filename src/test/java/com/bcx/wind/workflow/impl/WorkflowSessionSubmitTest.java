package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.WorkflowSession;
import com.bcx.wind.workflow.core.constant.NodeConfigConstant;
import com.bcx.wind.workflow.core.pojo.ApproveUser;
import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.core.pojo.Workflow;
import com.bcx.wind.workflow.core.pojo.WorkflowVariable;
import com.bcx.wind.workflow.entity.ProcessConfig;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.io.Resources;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class WorkflowSessionSubmitTest extends BaseTest {
    private String childOrderId = "085e6e77c1a845f3b190f5e1fa76785a";


    private void createProcess() throws IOException {
        //新增流程定义
        InputStream stream = Resources.getResourceAsStream("process/Context.xml");
        String processId = engine.repositoryService().processService().deploy(stream,"dms");

        //发布流程定义
        engine.repositoryService().processService().release(processId);

        //添加配置信息
        addNodeConfig();
    }


    private void addNodeConfig(){
        List<ProcessConfig> configList = engine.repositoryService().processConfigurationService().queryList(new QueryFilter().setProcessName("holiday").setNodeId("edit"));
        ProcessConfig config = configList.get(0);

        config.addNodeConfig(NodeConfigConstant.TASK_LINK,"/holiday/task/link")
                .addNodeConfig(NodeConfigConstant.TASK_CONTENT,"起草待办任务");

        engine.repositoryService().processConfigurationService().update(config);

        List<ProcessConfig> list = engine.repositoryService().processConfigurationService().queryList(new QueryFilter().setProcessName("holiday").setNodeId("routerEdit"));
        ProcessConfig nodeConfig = list.get(0);
        nodeConfig.addNodeConfig(NodeConfigConstant.TASK_LINK,"/holiday/task/link")
                .addNodeConfig(NodeConfigConstant.TASK_CONTENT,"子节点起草待办任务");
        nodeConfig.addApproveUser(new DefaultUser().setUserId("10002").setUserName("李四"))
                .addApproveUser(new DefaultUser().setUserId("10001").setUserName("张三"));
        engine.repositoryService().processConfigurationService().update(nodeConfig);

    }


    private void addRouterApproveNode(){
        List<ProcessConfig> list = engine.repositoryService().processConfigurationService().queryList(new QueryFilter().setProcessName("holiday").setNodeId("routerApprove"));
        ProcessConfig nodeConfig = list.get(0);
        nodeConfig.addNodeConfig(NodeConfigConstant.TASK_LINK,"/holiday/task/link")
                .addNodeConfig(NodeConfigConstant.TASK_CONTENT,"子节点审批待办任务");
        nodeConfig.addApproveUser(new DefaultUser().setUserId("10003").setUserName("王五"));

        engine.repositoryService().processConfigurationService().update(nodeConfig);
    }


    private void addRouterScribeTask(){
        List<ProcessConfig> list = engine.repositoryService().processConfigurationService().queryList(new QueryFilter().setProcessName("holiday").setNodeId("scribeTask"));
        ProcessConfig nodeConfig = list.get(0);
        nodeConfig.addNodeConfig(NodeConfigConstant.TASK_LINK,"/holiday/task/link")
                .addNodeConfig(NodeConfigConstant.TASK_CONTENT,"订阅待办任务");
        engine.repositoryService().processConfigurationService().update(nodeConfig);
    }

    private void addMonitor(){
        List<ProcessConfig> list = engine.repositoryService().processConfigurationService().queryList(new QueryFilter().setProcessName("holiday").setNodeId("monitor"));
        ProcessConfig nodeConfig = list.get(0);
        nodeConfig.addNodeConfig(NodeConfigConstant.TASK_LINK,"/holiday/task/link")
                .addNodeConfig(NodeConfigConstant.TASK_CONTENT,"班长待办任务");
        nodeConfig.addApproveUser(new DefaultUser().setUserId("10004").setUserName("张磊"));

        engine.repositoryService().processConfigurationService().update(nodeConfig);
    }

    private void addApproveA(){
        List<ProcessConfig> list = engine.repositoryService().processConfigurationService().queryList(new QueryFilter().setProcessName("holiday").setNodeId("approveA"));
        ProcessConfig nodeConfig = list.get(0);
        nodeConfig.addNodeConfig(NodeConfigConstant.TASK_LINK,"/holiday/task/link")
                .addNodeConfig(NodeConfigConstant.TASK_CONTENT,"approveA待办任务");
        nodeConfig.addApproveUser(new DefaultUser().setUserId("10005").setUserName("苏灿"));

        engine.repositoryService().processConfigurationService().update(nodeConfig);
    }


    private void addApproveB(){
        List<ProcessConfig> list = engine.repositoryService().processConfigurationService().queryList(new QueryFilter().setProcessName("holiday").setNodeId("approveB"));
        ProcessConfig nodeConfig = list.get(0);
        nodeConfig.addNodeConfig(NodeConfigConstant.TASK_LINK,"/holiday/task/link")
                .addNodeConfig(NodeConfigConstant.TASK_CONTENT,"approveB待办任务");
        nodeConfig.addApproveUser(new DefaultUser().setUserId("10006").setUserName("苏灿"));

        engine.repositoryService().processConfigurationService().update(nodeConfig);
    }

    private void addMinister(){
        List<ProcessConfig> list = engine.repositoryService().processConfigurationService().queryList(new QueryFilter().setProcessName("holiday").setNodeId("minister"));
        ProcessConfig nodeConfig = list.get(0);
        nodeConfig.addNodeConfig(NodeConfigConstant.TASK_LINK,"/holiday/task/link")
                .addNodeConfig(NodeConfigConstant.TASK_CONTENT,"部长待办任务");
        nodeConfig.addApproveUser(new DefaultUser().setUserId("10007").setUserName("季婷"));
        nodeConfig.addNodeConfig(NodeConfigConstant.SUBMIT_LINE,"disc_path1");

        engine.repositoryService().processConfigurationService().update(nodeConfig);
    }

    private void addFinance(){
        List<ProcessConfig> list = engine.repositoryService().processConfigurationService().queryList(new QueryFilter().setProcessName("holiday").setNodeId("finance"));
        ProcessConfig nodeConfig = list.get(0);
        nodeConfig.addNodeConfig(NodeConfigConstant.TASK_LINK,"/holiday/task/link")
                .addNodeConfig(NodeConfigConstant.TASK_CONTENT,"财务待办任务");
        nodeConfig.addApproveUser(new DefaultUser().setUserId("10008").setUserName("杨凌"));

        engine.repositoryService().processConfigurationService().update(nodeConfig);
    }


    private void addBoss(){
        List<ProcessConfig> list = engine.repositoryService().processConfigurationService().queryList(new QueryFilter().setProcessName("holiday").setNodeId("boss"));
        ProcessConfig nodeConfig = list.get(0);
        nodeConfig.addNodeConfig(NodeConfigConstant.TASK_LINK,"/holiday/task/link")
                .addNodeConfig(NodeConfigConstant.TASK_CONTENT,"董事长待办任务");
        nodeConfig.addApproveUser(new DefaultUser().setUserId("10009").setUserName("马云"));

        engine.repositoryService().processConfigurationService().update(nodeConfig);
    }

    private void addManager(){
        List<ProcessConfig> list = engine.repositoryService().processConfigurationService().queryList(new QueryFilter().setProcessName("holiday").setNodeId("manager"));
        ProcessConfig nodeConfig = list.get(0);
        nodeConfig.addNodeConfig(NodeConfigConstant.TASK_LINK,"/holiday/task/link")
                .addNodeConfig(NodeConfigConstant.TASK_CONTENT,"总经理待办任务");
        nodeConfig.addApproveUser(new DefaultUser().setUserId("10010").setUserName("张小龙"));

        engine.repositoryService().processConfigurationService().update(nodeConfig);
    }

    private void addApproveC(){
        List<ProcessConfig> list = engine.repositoryService().processConfigurationService().queryList(new QueryFilter().setProcessName("holiday").setNodeId("approveC"));
        ProcessConfig nodeConfig = list.get(0);
        nodeConfig.addNodeConfig(NodeConfigConstant.TASK_LINK,"/holiday/task/link")
                .addNodeConfig(NodeConfigConstant.TASK_CONTENT,"approveC待办任务");
        nodeConfig.addApproveUser(new DefaultUser().setUserId("10011").setUserName("王蓉"));

        engine.repositoryService().processConfigurationService().update(nodeConfig);
    }


    private Workflow buildFlow() throws IOException {
        //创建流程定义
        createProcess();

        WorkflowSession session = engine.openWorkflowSession("holiday");

        //创建工作流
        DefaultUser user = new DefaultUser()
                .setUserId("10003")
                .setUserName("张三");
        List<String> businessIds = new LinkedList<>();
        businessIds.add("00001");
        businessIds.add("00002");
        long start = System.currentTimeMillis();
        Workflow workflow = session.buildWorkflow(user,businessIds);
        long end = System.currentTimeMillis();
        System.out.println(end-start+" 毫秒");

        assert workflow!=null;
        return workflow;
    }


    @Test
    public void  buildFlowTest() throws IOException {
        //创建流程定义
        buildFlow();

        commit();
    }


    private void addSubmitConfig(){
       addNodeConfig();
       addApproveA();
       addApproveC();
       addApproveB();
       addRouterApproveNode();
       addRouterScribeTask();
       addMonitor();
       addMinister();
       addFinance();
       addBoss();
       addManager();
    }


    @Test
    public void submitOne() throws IOException {
        WorkflowSession session = engine.openWorkflowSession("holiday");

        addSubmitConfig();
        List<ApproveUser> users = new LinkedList<>();
        List<DefaultUser> defaultUsers = new LinkedList<>();
        defaultUsers.add(new DefaultUser().setUserId("10003").setUserName("周三"));
        defaultUsers.add(new DefaultUser().setUserId("10004").setUserName("六二"));
        users.add(new ApproveUser().setNodeId("routerEdit").setApproveUsers(defaultUsers));

        long start = System.currentTimeMillis();
        Workflow workflow1 = session.submit(orderId,new DefaultUser().setUserId("10003").setUserName("周三"),users);
        long end = System.currentTimeMillis();
        System.out.println(end-start+" 毫秒");

        assert workflow1!=null;
        commit();
    }


    @Test
    public void submitTwo() throws IOException {
        ObjectHelper.primaryKey();
        addSubmitConfig();
        WorkflowSession session = engine.openWorkflowSession("holiday");

        List<ApproveUser> users = new LinkedList<>();
        List<DefaultUser> defaultUsers = new LinkedList<>();
        defaultUsers.add(new DefaultUser().setUserId("10003").setUserName("周三"));
        defaultUsers.add(new DefaultUser().setUserId("10004").setUserName("六二"));
        users.add(new ApproveUser().setNodeId("routerApprove").setApproveUsers(defaultUsers));

        long start = System.currentTimeMillis();
        Workflow workflow1 = session.submit(orderId,new DefaultUser().setUserId("10003").setUserName("周三"),users);
        long end = System.currentTimeMillis();
        System.out.println(end-start+" 毫秒");

        assert workflow1!=null;
        commit();
    }


    @Test
    public void submitThree() throws IOException {
        addSubmitConfig();
        WorkflowSession session = engine.openWorkflowSession("holiday");

        List<ApproveUser> users = new LinkedList<>();
        List<DefaultUser> defaultUsers = new LinkedList<>();
        defaultUsers.add(new DefaultUser().setUserId("10005").setUserName("季婷"));

        List<DefaultUser> defaultUsers1 = new LinkedList<>();
        defaultUsers1.add(new DefaultUser().setUserId("10006").setUserName("王蓉"));
        users.add(new ApproveUser().setNodeId("monitor").setApproveUsers(defaultUsers));
        users.add(new ApproveUser().setNodeId("scribeTask").setApproveUsers(defaultUsers1));

        WorkflowVariable variable = new WorkflowVariable()
                .setOrderId(orderId)
                .setUser(new DefaultUser().setUserId("10003").setUserName("周三"))
                .setSystem("dms")
                .setSuggest("审批通过！")
                .setApproveUsers(users);

        Workflow workflow1 = session.submit(variable);
        assert workflow1!=null;

        commit();
    }


    @Test
    public void submitFour(){
        addSubmitConfig();
        WorkflowSession session = engine.openWorkflowSession("holiday");

        WorkflowVariable variable = new WorkflowVariable()
                .setOrderId(orderId)
                .setUser(new DefaultUser().setUserId("10006").setUserName("王蓉"))
                .setSystem("dms")
                .setSuggest("审批通过！");
        Workflow workflow1 = session.subScribe(variable);
        assert workflow1!=null;
        commit();
    }


    @Test
    public void submitFive(){
        addSubmitConfig();
        WorkflowSession session = engine.openWorkflowSession("holiday");

        List<ApproveUser> users = new LinkedList<>();
        List<DefaultUser> defaultUsers = new LinkedList<>();
        defaultUsers.add(new DefaultUser().setUserId("10007").setUserName("苏灿"));

        List<DefaultUser> defaultUsers1 = new LinkedList<>();
        defaultUsers1.add(new DefaultUser().setUserId("10008").setUserName("花千骨"));
        users.add(new ApproveUser().setNodeId("approveA").setApproveUsers(defaultUsers));
        users.add(new ApproveUser().setNodeId("approveC").setApproveUsers(defaultUsers1));

        WorkflowVariable variable = new WorkflowVariable()
                .setOrderId(orderId)
                .setUser(new DefaultUser().setUserId("10005").setUserName("季婷"))
                .setSystem("dms")
                .setSuggest("审批通过！")
                .setApproveUsers(users);
        Workflow workflow1 = session.submit(variable);
        assert workflow1!=null;
        commit();
    }


    @Test
    public void submitSix(){
        addSubmitConfig();
        WorkflowSession session = engine.openWorkflowSession("holiday");

        List<ApproveUser> users = new LinkedList<>();
        List<DefaultUser> defaultUsers = new LinkedList<>();
        defaultUsers.add(new DefaultUser().setUserId("10009").setUserName("楚留香"));

        users.add(new ApproveUser().setNodeId("minister").setApproveUsers(defaultUsers));

        WorkflowVariable variable = new WorkflowVariable()
                .setOrderId(orderId)
                .setUser(new DefaultUser().setUserId("10008").setUserName("花千骨"))
                .setSystem("dms")
                .setSuggest("审批通过！")
                .setApproveUsers(users);
        Workflow workflow1 = session.submit(variable);
        assert workflow1!=null;
        commit();
    }


    @Test
    public void submitSeven(){
        addSubmitConfig();
        WorkflowSession session = engine.openWorkflowSession("holiday");

        List<ApproveUser> users = new LinkedList<>();
        List<DefaultUser> defaultUsers = new LinkedList<>();
        defaultUsers.add(new DefaultUser().setUserId("100010").setUserName("孙悟空"));

        users.add(new ApproveUser().setNodeId("approveB").setApproveUsers(defaultUsers));

        WorkflowVariable variable = new WorkflowVariable()
                .setOrderId(orderId)
                .setUser(new DefaultUser().setUserId("10007").setUserName("苏灿"))
                .setSystem("dms")
                .setSuggest("审批通过！")
                .setApproveUsers(users);
        Workflow workflow1 = session.submit(variable);
        assert workflow1!=null;
        commit();
    }

    @Test
    public void submitEight(){
        addSubmitConfig();
        WorkflowSession session = engine.openWorkflowSession("holiday");

        List<ApproveUser> users = new LinkedList<>();
        List<DefaultUser> defaultUsers = new LinkedList<>();
        defaultUsers.add(new DefaultUser().setUserId("100011").setUserName("武松"));

        users.add(new ApproveUser().setNodeId("minister").setApproveUsers(defaultUsers));

        WorkflowVariable variable = new WorkflowVariable()
                .setOrderId(orderId)
                .setUser(new DefaultUser().setUserId("100010").setUserName("孙悟空"))
                .setSystem("dms")
                .setSuggest("审批通过！")
                .setApproveUsers(users);
        Workflow workflow1 = session.submit(variable);
        assert workflow1!=null;
        commit();
    }

    @Test
    public void submitNine(){
        addSubmitConfig();
        WorkflowSession session = engine.openWorkflowSession("holiday");

        List<ApproveUser> users = new LinkedList<>();
        List<DefaultUser> defaultUsers = new LinkedList<>();
        defaultUsers.add(new DefaultUser().setUserId("100012").setUserName("唐僧"));

        users.add(new ApproveUser().setNodeId("finance").setApproveUsers(defaultUsers));

        WorkflowVariable variable = new WorkflowVariable()
                .setOrderId(orderId)
                .setUser(new DefaultUser().setUserId("100011").setUserName("武松"))
                .setSystem("dms")
                .setSuggest("审批通过！")
                .setApproveUsers(users);
        Workflow workflow1 = session.submit(variable);
        assert workflow1!=null;
        commit();
    }


    @Test
    public void submitTen(){
        addSubmitConfig();
        WorkflowSession session = engine.openWorkflowSession("holiday");

        List<ApproveUser> users = new LinkedList<>();
        List<DefaultUser> defaultUsers = new LinkedList<>();
        defaultUsers.add(new DefaultUser().setUserId("100013").setUserName("马云"));

        users.add(new ApproveUser().setNodeId("boss").setApproveUsers(defaultUsers));

        WorkflowVariable variable = new WorkflowVariable()
                .setOrderId(orderId)
                .setUser(new DefaultUser().setUserId("100012").setUserName("唐僧"))
                .setSystem("dms")
                .setSuggest("审批通过！")
                .setApproveUsers(users);
        long start = System.currentTimeMillis();
        Workflow workflow1 = session.submit(variable);
        long end = System.currentTimeMillis();
        System.out.println(end-start+"毫秒");
        assert workflow1!=null;
        commit();
    }


    @Test
    public void submitEleven(){
        addSubmitConfig();
        WorkflowSession session = engine.openWorkflowSession("holiday");

        WorkflowVariable variable = new WorkflowVariable()
                .setOrderId(orderId)
                .setUser(new DefaultUser().setUserId("100013").setUserName("马云"))
                .setSystem("dms")
                .setSuggest("审批通过！");

        long start = System.currentTimeMillis();
        Workflow workflow1 = session.submit(variable);
        long end = System.currentTimeMillis();
        System.out.println(end-start+"毫秒");
        assert workflow1!=null;
        commit();
    }
}
