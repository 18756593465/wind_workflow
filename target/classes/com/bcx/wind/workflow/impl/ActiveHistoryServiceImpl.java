package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.Access;
import com.bcx.wind.workflow.AccessService;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.ActiveHistoryService;
import com.bcx.wind.workflow.entity.ActiveHistory;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.TimeHelper;
import com.bcx.wind.workflow.message.MessageHelper;
import com.bcx.wind.workflow.message.MsgConstant;

import java.util.List;

import static com.bcx.wind.workflow.core.constant.Constant.ARRAY;

/**
 * 执行履历模块
 */
public class ActiveHistoryServiceImpl extends AccessService implements ActiveHistoryService {

    private static final String ACTIVE_HISTORY = "activeHistory";

    public ActiveHistoryServiceImpl(Access access) {
        super(access);
    }

    @Override
    public List<ActiveHistory> queryList(QueryFilter filter) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w001),filter);
        //查询
        return access.selectActiveHistoryList(filter);
    }

    @Override
    public List<ActiveHistory> queryList(QueryFilter filter, FlowPage<ActiveHistory> page) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w001),filter);
        return access.selectActiveHistoryList(filter,page);
    }

    @Override
    public ActiveHistory queryById(String id) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w001),id);
        return access.getActiveHistoryById(id);
    }

    @Override
    public int insert(ActiveHistory activeHistory) {
        checkActiveHistory(activeHistory);
        activeHistory.setId(ObjectHelper.primaryKey()).
                setCreateTime(TimeHelper.getNow());

        return access.insertActiveHistory(activeHistory);
    }

    @Override
    public int update(ActiveHistory activeHistory) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w002,ACTIVE_HISTORY),activeHistory.getId());
        checkActiveHistory(activeHistory);
        if(ObjectHelper.isEmpty(activeHistory.getApproveUserVariable())){
            activeHistory.setApproveUserVariable(ARRAY);
        }
        return access.updateActiveHistory(activeHistory);
    }

    @Override
    public int deleteById(String id) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w002,ACTIVE_HISTORY), id);
        return access.removeActiveHistoryById(id);
    }

    @Override
    public int deleteByIds(List<String> ids) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w002,ACTIVE_HISTORY), ids);
        return access.removeActiveHistoryByIds(ids);
    }

    private void checkActiveHistory(ActiveHistory activeHistory){
        Assert.hasEmpty(MessageHelper.getMsg(MsgConstant.w003,ACTIVE_HISTORY),activeHistory.getTaskId(),activeHistory.getTaskName(),
                activeHistory.getTaskDisplayName(),activeHistory.getProcessId(),activeHistory.getOrderId(),
                activeHistory.getProcessName(),activeHistory.getProcessDisplayName(),activeHistory.getTaskType());

    }
}
