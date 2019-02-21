package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.ActiveHistory;
import com.bcx.wind.workflow.entity.CompleteHistory;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class CompleteHistoryServiceTest extends BaseTest {


    private CompleteHistory completeHistory(){
        return  new CompleteHistory()
                .setId(ObjectHelper.primaryKey())
                .setOrderId(ObjectHelper.primaryKey())
                .setTaskId(ObjectHelper.primaryKey())
                .setTaskName("approve")
                .setTaskDisplayName("审批")
                .setProcessId(ObjectHelper.primaryKey())
                .setProcessName("holidayProcess")
                .setProcessDisplayName("请假流程")
                .setOperate("提交")
                .setSuggest("同意通过")
                .setApproveTime("2018-04-02 12:21:31")
                .setActorId("10003")
                .setActorName("张三")
                .setApproveId("10003")
                .setApproveName("张三")
                .setCreateTime("2018-04-01 11:12:42")
                .setApproveUserVariable("[]")
                .setTaskType("会签")
                .setSystem("dms")
                .setSubmitUserVariable("{}");
    }

    private ActiveHistory activeHistory(){
        return  new ActiveHistory()
                .setId(ObjectHelper.primaryKey())
                .setOrderId(ObjectHelper.primaryKey())
                .setTaskId(ObjectHelper.primaryKey())
                .setTaskName("approve")
                .setTaskDisplayName("审批")
                .setProcessId(ObjectHelper.primaryKey())
                .setProcessName("holidayProcess")
                .setProcessDisplayName("请假流程")
                .setOperate("提交")
                .setSuggest("同意通过")
                .setApproveTime("2018-04-02 12:21:31")
                .setActorId("10003")
                .setActorName("张三")
                .setApproveId("10003")
                .setApproveName("张三")
                .setCreateTime("2018-04-01 11:12:42")
                .setApproveUserVariable("[]")
                .setTaskType("会签")
                .setSystem("dms")
                .setSubmitUserVariable("{}");
    }



    @Test
    public void add(){
        CompleteHistory history = completeHistory();
        int ret = engine.historyService().completeHistoryService().insert(history);
        assert  ret >= 1;
    }

    @Test
    public void addList(){
        List<ActiveHistory> historyList = new LinkedList<>();
        historyList.add(activeHistory());

        int ret = engine.historyService().completeHistoryService().insertList(historyList);
        assert ret >= 1;
    }

    @Test
    public void update(){
        CompleteHistory history = completeHistory();
        engine.historyService().completeHistoryService().insert(history);

        history.setActorId("2004");
        int ret = engine.historyService().completeHistoryService().update(history);
        assert ret >= 1;
    }


    @Test
    public void delete(){
        CompleteHistory history = completeHistory();
        engine.historyService().completeHistoryService().insert(history);

        int ret = engine.historyService().completeHistoryService().deleteById(history.getId());
        assert ret >= 1;
    }


    @Test
    public void deleteByIds(){
        CompleteHistory history = completeHistory();
        engine.historyService().completeHistoryService().insert(history);

        List<String> ids = new LinkedList<>();
        ids.add(history.getId());

        int ret = engine.historyService().completeHistoryService().deleteByIds(ids);
        assert ret >= 1;
    }

    @Test
    public void queryOne(){
        CompleteHistory history = completeHistory();
        engine.historyService().completeHistoryService().insert(history);

        CompleteHistory completeHistory = engine.historyService().completeHistoryService().queryById(history.getId());
        assert completeHistory!=null;
    }

    @Test
    public void query(){
        List<CompleteHistory> histories = engine.historyService().completeHistoryService().queryList(new QueryFilter());
        System.out.println(histories.size());
    }

    @Test
    public void queryList(){
        FlowPage<CompleteHistory> page = new FlowPage<>();
        List<CompleteHistory> completeHistories = engine.historyService().completeHistoryService().queryList(new QueryFilter(),page);
        System.out.println(completeHistories.size());
    }
}
