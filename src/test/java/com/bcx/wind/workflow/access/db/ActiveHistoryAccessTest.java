package com.bcx.wind.workflow.access.db;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.ActiveHistory;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * 持久层测试
 */
public class ActiveHistoryAccessTest extends BaseTest {

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
    public void activeHistoryAdd(){
        ActiveHistory history = createActiveHistory();
        int ret = access.insertActiveHistory(history);
        assert ret >= 1;
        commit();
    }


    @Test
    public void  activeHistoryUpdate(){
        ActiveHistory history = createActiveHistory();
        access.insertActiveHistory(history);
        history.setSystem("eqms");
        int updateret = access.updateActiveHistory(history);
        assert updateret >= 1;
        commit();
    }

    @Test
    public void activeHistoryDelete(){
        ActiveHistory history = createActiveHistory();
        access.insertActiveHistory(history);
        int deleteRet = access.removeActiveHistoryById(history.getId());
        assert deleteRet >= 1;
    }

    @Test
    public void activeHistorySearch(){
        QueryFilter filter = new QueryFilter();
        List<ActiveHistory> activeHistoryList = access.selectActiveHistoryList(filter);
        System.out.println(activeHistoryList.size());
    }


    @Test
    public void activeHistorySearchPage(){
        QueryFilter filter = new QueryFilter();
        FlowPage<ActiveHistory> page = new FlowPage<>();
        List<ActiveHistory> histories = access.selectActiveHistoryList(filter,page);
        System.out.println(histories.size());
    }


    @Test
    public void activeHistoryQueryOne(){
        ActiveHistory history = createActiveHistory();
        access.insertActiveHistory(history);
        ActiveHistory activeHistory = access.getActiveHistoryById(history.getId());
        assert activeHistory!=null;
    }

    @Test
    public void activeHistoryDeleteIds(){
        ActiveHistory history = createActiveHistory();
        ActiveHistory history1 = createActiveHistory();
        access.insertActiveHistory(history);
        access.insertActiveHistory(history1);

        List<String> ids = new LinkedList<>();
        ids.add(history.getId());
        ids.add(history1.getId());

        int ret = access.removeActiveHistoryByIds(ids);
        assert ret >= 1;
    }





}
