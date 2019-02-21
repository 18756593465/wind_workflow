package com.bcx.wind.workflow.access.db;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.ActiveHistory;
import com.bcx.wind.workflow.entity.CompleteHistory;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CompleteHistoryAccessTest extends BaseTest {


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
        int ret = access.insertCompleteHistory(history);
        assert ret >= 1;
    }

    @Test
    public void addList(){
        ActiveHistory history = activeHistory();
        ActiveHistory history1 = activeHistory();

        List<ActiveHistory> histories = new LinkedList<>();
        histories.add(history);
        histories.add(history1);

        int ret = access.insertCompleteHistoryList(histories);
        assert ret >= 1;
    }

    @Test
    public void remove(){
        CompleteHistory history = completeHistory();
        access.insertCompleteHistory(history);

        int ret =  access.removeCompleteHistoryById(history.getId());
        assert ret >= 1;
    }

    @Test
    public void removeIds(){
        ActiveHistory history = activeHistory();
        ActiveHistory history1 = activeHistory();

        List<ActiveHistory> histories = new LinkedList<>();
        histories.add(history);
        histories.add(history1);

        access.insertCompleteHistoryList(histories);

        List<String> ids = histories.stream().map(ActiveHistory::getId).collect(Collectors.toList());

        int ret = access.removeCompleteHistoryIds(ids);
        assert ret >= 1;
    }

    @Test
    public void update(){
        CompleteHistory history = completeHistory();
        access.insertCompleteHistory(history);
        history.setSystem("eqms");

        int ret = access.updateCompleteHistory(history);
        assert ret >= 1;
    }

    @Test
    public void queryOne(){
        CompleteHistory history = completeHistory();
        access.insertCompleteHistory(history);

        CompleteHistory completeHistory = access.getCompleteHistoryById(history.getId());
        assert completeHistory!=null ;
    }



    @Test
    public void query(){
        List<CompleteHistory> histories = access.selectCompleteHistoryList(new QueryFilter());
        System.out.println(histories.size());
    }


    @Test
    public void queryPage(){
        FlowPage<CompleteHistory> historyFlowPage = new FlowPage<>();
        List<CompleteHistory> histories = access.selectCompleteHistoryList(new QueryFilter(),historyFlowPage);

        System.out.println(histories.size());

    }


}
