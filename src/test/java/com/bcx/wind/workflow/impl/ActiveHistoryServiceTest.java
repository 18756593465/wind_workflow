package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.ActiveHistory;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class ActiveHistoryServiceTest extends BaseTest {

    private ActiveHistory createActiveHistory(){
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
        ActiveHistory history = createActiveHistory();
        int ret =  engine.historyService().activeHistoryService().insert(history);
        assert ret >= 1;

        commit();
    }


    @Test
    public void update(){
        ActiveHistory history = createActiveHistory();
        engine.historyService().activeHistoryService().insert(history);

        history.setActorName("李四");
        int ret = engine.historyService().activeHistoryService().update(history);
        assert ret >= 1;
    }

    @Test
    public void delete(){
        ActiveHistory history = createActiveHistory();
        engine.historyService().activeHistoryService().insert(history);

        int ret = engine.historyService().activeHistoryService().deleteById(history.getId());
        assert ret >= 1;
    }

    @Test
    public void deleteByIds(){
        ActiveHistory history = createActiveHistory();
        engine.historyService().activeHistoryService().insert(history);

        List<String> ids = new LinkedList<>();
        ids.add(history.getId());

        int ret = engine.historyService().activeHistoryService().deleteByIds(ids);
        assert ret >= 1;
    }

    @Test
    public void queryOne(){
        ActiveHistory history = createActiveHistory();
        engine.historyService().activeHistoryService().insert(history);

        ActiveHistory activeHistory = engine.historyService().activeHistoryService().queryById(history.getId());
        assert activeHistory!=null;
    }


    @Test
    public void query(){
        QueryFilter filter = new QueryFilter();

        List<ActiveHistory> histories = engine.historyService().activeHistoryService().queryList(filter);
        System.out.println(histories.size());
    }

    @Test
    public void queryList(){
        FlowPage<ActiveHistory> page = new FlowPage<>();

        List<ActiveHistory> histories = engine.historyService().activeHistoryService().queryList(new QueryFilter(),page);
        System.out.println(histories.size());
    }
}
