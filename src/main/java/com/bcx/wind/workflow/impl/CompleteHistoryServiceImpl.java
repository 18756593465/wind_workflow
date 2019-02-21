package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.Access;
import com.bcx.wind.workflow.AccessService;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.CompleteHistoryService;
import com.bcx.wind.workflow.entity.ActiveHistory;
import com.bcx.wind.workflow.entity.CompleteHistory;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.message.MsgConstant;

import java.util.List;

/**
 * 完成履历模块
 */
public class CompleteHistoryServiceImpl extends AccessService implements CompleteHistoryService {

    public CompleteHistoryServiceImpl(Access access) {
        super(access);
    }


    @Override
    public List<CompleteHistory> queryList(QueryFilter filter) {
        Assert.notEmpty(MsgConstant.w001,filter);
        return access.selectCompleteHistoryList(filter);
    }

    @Override
    public List<CompleteHistory> queryList(QueryFilter filter, FlowPage<CompleteHistory> page) {
        Assert.notEmpty(MsgConstant.w001,filter);
        return access.selectCompleteHistoryList(filter,page);
    }

    @Override
    public CompleteHistory queryById(String id) {
        Assert.notEmpty(MsgConstant.w001,id);
        return access.getCompleteHistoryById(id);
    }

    @Override
    public int insert(CompleteHistory completeHistory) {
        return access.insertCompleteHistory(completeHistory);
    }

    @Override
    public int insertList(List<ActiveHistory> activeHistories) {
        return access.insertCompleteHistoryList(activeHistories);
    }

    @Override
    public int update(CompleteHistory completeHistory) {
        return access.updateCompleteHistory(completeHistory);
    }

    @Override
    public int deleteById(String id) {
        return access.removeCompleteHistoryById(id);
    }

    @Override
    public int deleteByIds(List<String> ids) {
        return access.removeCompleteHistoryIds(ids);
    }

}
